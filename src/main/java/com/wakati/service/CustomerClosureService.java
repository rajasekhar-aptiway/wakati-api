package com.wakati.service;

import com.wakati.entity.AdminActionLog;
import com.wakati.entity.CustomerClosureRequest;
import com.wakati.entity.User;
import com.wakati.entity.Wallet;
import com.wakati.enums.ActionType;
import com.wakati.enums.Status;
import com.wakati.enums.UserType;
import com.wakati.model.response.ResponseBuilder;
import com.wakati.repository.AdminActionLogRepository;
import com.wakati.repository.CustomerClosureRequestRepository;
import com.wakati.repository.UserRepository;
import com.wakati.repository.WalletRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@Transactional
public class CustomerClosureService {

    @Autowired
    private  UserRepository userRepository;
    @Autowired
    private  WalletRepository walletRepository;
    @Autowired
    private CustomerClosureRequestRepository closureRepository;
    @Autowired
    private AdminActionLogRepository adminActionLogRepository;

    @Autowired
    private ResponseBuilder responseBuilder;

    public Map<String, Object> createClosureRequest(String customerId, String authUserId) {

        // ================= CALLER =================
        User caller = userRepository.findByUserId(authUserId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserType callerType = caller.getUserType();

        boolean isAgent = List.of(UserType.FRONT_DESK, UserType.PARTNER_AGENT).contains(callerType);

        if (!callerType.equals(UserType.CUSTOMER) && !isAgent) {
            throw new RuntimeException("Only customer or agent allowed");
        }

        // ================= CUSTOMER ID =================


        if (callerType.equals(UserType.CUSTOMER)) {
            customerId = authUserId;
        } else {
            if (customerId == null || customerId.isBlank()) {
                throw new RuntimeException("customer_id required");
            }
            customerId = customerId.trim();
        }

        // ================= CUSTOMER VALIDATION =================
        User customer = userRepository.findByUserId(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        if (!UserType.CUSTOMER.equals(customer.getUserType())) {
            throw new RuntimeException("Not a customer");
        }

        if (Status.CLOSED.equals(customer.getStatus())) {
            throw new RuntimeException("Account already closed");
        }

        // ================= DUPLICATE REQUEST =================
        boolean exists = closureRepository
                .existsByCustomerAndStatus(customer, Status.PENDING);

        if (exists) {
            throw new RuntimeException("Closure already pending");
        }

        // ================= WALLET CHECK =================
        Wallet wallet = walletRepository.findByOwner_UserId(customerId)
                .orElseThrow(() -> new RuntimeException("Wallet not found"));

        if (wallet.getBalance().compareTo(BigDecimal.ZERO) != 0) {
            throw new RuntimeException("Wallet balance must be zero");
        }

        // ================= CREATE REQUEST =================
        CustomerClosureRequest closure = new CustomerClosureRequest();
        closure.setClosureRequestId(UUID.randomUUID().toString());
        closure.setCustomer(customer);
        closure.setRequestedBy(caller);
        closure.setRequestedByType(callerType.name());
        closure.setStatus(Status.PENDING);

        closureRepository.save(closure);

        // ================= UPDATE USER =================
        customer.setStatus(Status.CLOSURE_PENDING);
        userRepository.save(customer);



        return Map.of(
                "code", 200,
                "message", "Closure request submitted",
                "closure_request_id", closure.getClosureRequestId(),
                "customer_id", customerId,
                "requested_by_type", callerType
        );
    }


    public Map<String, Object> processClosure(String closureRequestId,
                                              String action,
                                              String remarks,
                                              String authUserId) {

        // ✅ Caller validation
        User adjudicator = userRepository.findByUserId(authUserId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (adjudicator.getUserType() != UserType.ADJUDICATOR) {
            throw new RuntimeException("Only adjudicator allowed");
        }

        // ✅ Fetch request
        CustomerClosureRequest closure = closureRepository.findByClosureRequestId(closureRequestId)
                .orElseThrow(() -> new RuntimeException("Closure request not found"));

        if (!Status.PENDING.equals(closure.getStatus())) {
            throw new RuntimeException("Closure not pending");
        }

        User customer = closure.getCustomer();

        // ================= REJECT =================
        if ("REJECT".equalsIgnoreCase(action)) {

            closure.setStatus(Status.REJECTED);
            closure.setApprovedBy(adjudicator);
            closure.setApprovedAt(LocalDateTime.now());
            closure.setRemarks(remarks);

            closureRepository.save(closure);

            // revert user status
            customer.setStatus(Status.APPROVED);
            userRepository.save(customer);

            // audit log
            saveAudit(adjudicator, "CLOSURE_REJECTED", customer.getUserId(), remarks);

            return Map.of(
                    "code", 200,
                    "message", "Closure rejected"
            );
        }

        // ================= APPROVE =================

        // re-check wallet
        Wallet wallet = walletRepository.findByOwner_UserId(customer.getUserId())
                .orElseThrow(() -> new RuntimeException("Wallet not found"));

        if (wallet.getBalance().compareTo(BigDecimal.ZERO) != 0) {
            throw new RuntimeException("Wallet must be zero");
        }

        // close account
        customer.setStatus(Status.CLOSED);
        userRepository.save(customer);

        // update request
        closure.setStatus(Status.APPROVED);
        closure.setApprovedBy(adjudicator);
        closure.setApprovedAt(LocalDateTime.now());
        closure.setRemarks(remarks);

        closureRepository.save(closure);

        // audit log
        saveAudit(adjudicator, "ACCOUNT_CLOSURE_APPROVED", customer.getUserId(), remarks);

        return Map.of(
                "code", 200,
                "message", "Closure approved"
        );
    }

    // ===========================
    // AUDIT LOG
    // ===========================
    private void saveAudit(User performedBy, String action, String targetUserId, String remarks) {

        AdminActionLog log = new AdminActionLog();
        log.setPerformedBy(performedBy.getUserId());
        log.setActionType(ActionType.valueOf(action));
        log.setTargetUserId(targetUserId);
        log.setDetails(remarks);

        adminActionLogRepository.save(log);
    }
}