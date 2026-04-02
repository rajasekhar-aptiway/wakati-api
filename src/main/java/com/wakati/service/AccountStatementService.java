package com.wakati.service;

import com.wakati.config.UserContextHolder;
import com.wakati.entity.User;
import com.wakati.exception.WakatiException;
import com.wakati.model.request.AccountStatementRequest;
import com.wakati.model.response.StatementProjection;
import com.wakati.repository.AccountStatementRepository;
import com.wakati.repository.UserRepository;
import com.wakati.transaction.model.TransactionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static com.wakati.I18NConstants.*;

@Service
public class AccountStatementService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AccountStatementRepository repository;

    public Map<String, Object> getStatement(AccountStatementRequest req) {

        // ✅ Validate
        if (req.getUserId() == null || req.getUserId().isBlank()) {
            throw new WakatiException(USER_ID_REQUIRED);
        }

        String userId = req.getUserId();

        // ✅ Auth user
        User authUser = UserContextHolder.getUser();

        // ✅ Authorization
        validateAccess(authUser, userId);

        // ✅ Target user
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new WakatiException(USER_NOT_FOUND));

        // ✅ Date handling (NO DATE() in SQL)
        LocalDate fromDate = null;
        LocalDate toDate = null;

        if (req.getFromDate() != null && req.getToDate() != null) {
            fromDate = req.getFromDate();
            toDate = req.getToDate().plusDays(1);
        } else if (req.getPeriod() != null) {

            LocalDate today = LocalDate.now();

            switch (req.getPeriod().toLowerCase()) {
                case "day" -> {
                    fromDate = today;
                    toDate = today.plusDays(1);
                }
                case "week" -> {
                    fromDate = today.minusDays(6);
                    toDate = today.plusDays(1);
                }
                case "month" -> {
                    fromDate = today.minusDays(29);
                    toDate = today.plusDays(1);
                }
            }
        }

        // ✅ Pagination
        int page = Math.max(1, req.getPage());
        int size = Math.min(100, Math.max(1, req.getPageSize()));
        int offset = (page - 1) * size;

        // ✅ Fetch data (DB optimized)
        List<StatementProjection> rows = repository.fetchOptimizedStatement(
                userId,
                fromDate,
                toDate,
                size,
                offset
        );

        long total = repository.countStatement(userId);

        // ✅ Map
        List<Map<String, Object>> transactions = rows.stream()
                .map(r -> mapStatementProjection(r, userId))
                .toList();

        // ✅ Summary calculation (lightweight)
        BigDecimal totalIn = BigDecimal.ZERO;
        BigDecimal totalOut = BigDecimal.ZERO;
        BigDecimal totalCommission = BigDecimal.ZERO;
        int commissionCount = 0;

        for (StatementProjection r : rows) {

            BigDecimal amount = r.getAmount() != null ? r.getAmount() : BigDecimal.ZERO;

            if ("CREDIT".equalsIgnoreCase(r.getDirection())) {
                totalIn = totalIn.add(amount);
            } else {
                totalOut = totalOut.add(amount);
            }

            if (r.getCommissionAmount() != null &&
                    r.getCommissionAmount().compareTo(BigDecimal.ZERO) > 0) {

                totalCommission = totalCommission.add(r.getCommissionAmount());
                commissionCount++;
            }
        }

        BigDecimal currentBalance = rows.isEmpty()
                ? BigDecimal.ZERO
                : (rows.get(0).getBalanceAfter() != null
                ? rows.get(0).getBalanceAfter()
                : BigDecimal.ZERO);

        int totalPages = (int) Math.ceil((double) total / size);

        // ✅ Final Response
        return Map.of(
                "code", 200,
                "user_id", user.getUserId(),
                "user_name", user.getFullName(),
                "mobile_no", user.getMobileNo(),
                "user_type", user.getUserType(),

                "current_balance", currentBalance,

                "period", Map.of(
                        "from", fromDate,
                        "to", toDate
                ),

                "summary", Map.of(
                        "total_in", totalIn,
                        "total_out", totalOut,
                        "net", totalIn.subtract(totalOut),
                        "transaction_count", total,
                        "total_commission", totalCommission,
                        "commission_count", commissionCount
                ),

                "transactions", transactions,

                "pagination", Map.of(
                        "page", page,
                        "page_size", size,
                        "total", total,
                        "total_pages", totalPages
                )
        );
    }

    private void validateAccess(User authUser, String targetUserId) {

        if (authUser.getUserId().equals(targetUserId)) return;

        String callerType = authUser.getUserType().name();

        User target = userRepository.findByUserId(targetUserId)
                .orElseThrow(() -> new WakatiException(USER_NOT_FOUND));

        String targetType = target.getUserType().name();

        boolean allowed = switch (callerType) {

            case "ADMIN", "CFO" -> true;

            case "RECEIVER" ->
                    List.of("SUPER_DEALER", "DEALER", "PARTNER_AGENT", "FRONT_DESK")
                            .contains(targetType);

            case "SUPER_DEALER" ->
                    "DEALER".equals(targetType) &&
                            userRepository.existsDealerAssignment(authUser.getUserId(), targetUserId);

            case "PARTNER_AGENT" ->
                    List.of("PARTNER_AGENT_SUPER_DEALER", "PARTNER_AGENT_DEALER")
                            .contains(targetType);

            case "PARTNER_AGENT_SUPER_DEALER" ->
                    "PARTNER_AGENT_DEALER".equals(targetType) &&
                            userRepository.existsDealerAssignment(authUser.getUserId(), targetUserId);

            default -> false;
        };

        if (!allowed) {
            throw new WakatiException(ACCESS_DENIED);
        }
    }

    public Map<String, Object> mapStatementProjection(StatementProjection p, String userId) {

        BigDecimal amount = p.getAmount();
        BigDecimal commission = p.getCommissionAmount() != null
                ? p.getCommissionAmount()
                : BigDecimal.ZERO;

        if (p.getCommissionInAmount() != null && p.getCommissionInAmount() == 1) {
            amount = amount.subtract(commission);
        }

        String counterpartyId =
                userId.equals(p.getSourceUserId()) ? p.getTargetUserId() : p.getSourceUserId();

        String counterpartyName =
                userId.equals(p.getSourceUserId()) ? p.getTargetName() : p.getSourceName();

        String counterpartyMobile =
                userId.equals(p.getSourceUserId()) ? p.getTargetMobile() : p.getSourceMobile();

        return Map.of(
                "txn_id", p.getTxnId(),
                "date", p.getCreatedAt(),
                "txn_type", p.getTxnType(),
                "direction", "CREDIT".equalsIgnoreCase(p.getDirection()) ? "IN" : "OUT",
                "amount", amount,
                "commission", commission,
                "balance_after", p.getBalanceAfter(),
                "counterparty_id", counterpartyId,
                "counterparty_name", counterpartyName != null ? counterpartyName : "SYSTEM",
                "counterparty_mobile", counterpartyMobile != null ? counterpartyMobile : ""
        );
    }
}