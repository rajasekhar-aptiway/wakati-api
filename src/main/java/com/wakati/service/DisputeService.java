package com.wakati.service;

import com.wakati.config.UserContextHolder;
import com.wakati.entity.User;
import com.wakati.exception.WakatiException;
import com.wakati.model.request.DisputeRequest;
import com.wakati.repository.TransactionDisputeRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static com.wakati.I18NConstants.*;

@Service
@Transactional
public class DisputeService {

    @Autowired
    private TransactionDisputeRepository repo;

    public Map<String, Object> handle(DisputeRequest req, HttpServletRequest httpRequest) {

        String action = Optional.ofNullable(req.getAction()).orElse("raise").toLowerCase();

        User user = UserContextHolder.getUser();
        String userId = user.getUserId();
        String userType = user.getUserType().name();

        List<String> adminRoles = List.of("ADMIN", "CFO", "ADJUDICATOR");
        boolean isAdmin = adminRoles.contains(userType);

        return switch (action) {
            case "raise" -> raise(req, userId);
            case "list" -> list(req, userId, isAdmin);
            case "update" -> update(req, userId, isAdmin,httpRequest.getRemoteAddr());
            default -> throw new WakatiException(INVALID_DISPUTE_ACTION);
        };
    }

    private Map<String, Object> raise(DisputeRequest req, String userId) {

        if (StringUtils.isEmpty(req.getTxnId()) || StringUtils.isEmpty(req.getReason())) {
            throw new WakatiException(MISSING_TXN_ID_AND_REASON);
        }

        // ✅ validate txn ownership
        boolean exists = repo.validateTransactionOwnership(req.getTxnId(), userId);

        if (!exists) {
            throw new WakatiException(TXN_NOT_FOUND);
        }

        // ✅ duplicate check
        boolean alreadyExists =
                repo.existsOpenDispute(req.getTxnId(), userId);

        if (alreadyExists) {
            throw new WakatiException(DISPUTE_ALREADY_EXIST);
        }

        String disputeId = UUID.randomUUID().toString();

        repo.insertDispute(
                disputeId,
                req.getTxnId(),
                userId,
                req.getReason()
        );

        return Map.of(
                "code", 200,
                "message", "Dispute raised successfully",
                "trace_id", MDC.get("traceId"),
                "dispute_id", disputeId,
                "txn_id", req.getTxnId(),
                "status", "OPEN"
        );
    }

    private Map<String, Object> list(DisputeRequest req, String userId, boolean isAdmin) {

        int page = Math.max(1, Optional.ofNullable(req.getPage()).orElse(1));
        int size = Math.max(1, Math.min(100, Optional.ofNullable(req.getPageSize()).orElse(20)));
        int offset = (page - 1) * size;

        String status = Optional.ofNullable(req.getStatus()).orElse("").toUpperCase();

        long total = repo.countDisputes(userId, status, isAdmin);

        List<Map<String, Object>> rows =
                repo.fetchDisputes(userId, status, isAdmin, size, offset);

        return Map.of(
                "code", 200,
                "trace_id", MDC.get("traceId"),
                "disputes", rows,
                "pagination", Map.of(
                        "page", page,
                        "page_size", size,
                        "total", total,
                        "total_pages", total > 0 ? (int) Math.ceil((double) total / size) : 0
                )
        );
    }

    private Map<String, Object> update(DisputeRequest req, String userId, boolean isAdmin,String ip) {

        if (!isAdmin) {
            throw new WakatiException(HttpStatus.FORBIDDEN,ACCESS_DENIED);
        }

        List<String> valid = List.of("UNDER_REVIEW", "RESOLVED", "REJECTED");

        if (StringUtils.isEmpty(req.getDisputeId()) || !valid.contains(req.getStatus())) {
            throw new WakatiException(INVALID_STATUS);
        }

        String currentStatus = repo.getDisputeStatus(req.getDisputeId());

        if (currentStatus == null) {
            throw new WakatiException(HttpStatus.NOT_FOUND,DISPUTE_NOT_FOUND);
        }

        if (List.of("RESOLVED", "REJECTED").contains(currentStatus)) {
            throw new WakatiException(HttpStatus.ALREADY_REPORTED, DISPUTE_CLOSED);

        }

        repo.updateDispute(
                req.getStatus(),
                userId,
                req.getResolution(),
                req.getDisputeId()
        );

        // ✅ ADMIN AUDIT LOG
        repo.insertAdminLog(
                userId,
                req.getDisputeId(),
                req.getStatus(),
                req.getResolution(),
                ip
        );

        return Map.of(
                "code", 200,
                "message", "Dispute updated",
                "trace_id", MDC.get("traceId"),
                "dispute_id", req.getDisputeId(),
                "status", req.getStatus()
        );
    }
}