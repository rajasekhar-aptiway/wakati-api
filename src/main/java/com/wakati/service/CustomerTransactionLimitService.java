package com.wakati.service;

import com.wakati.entity.CustomerTransferLimits;
import com.wakati.entity.User;
import com.wakati.enums.UserType;
import com.wakati.model.request.DTO.TransactionLimitRequest;
import com.wakati.model.response.CustomerTransferLimitProjection;
import com.wakati.repository.CustomerTransferLimitsRepository;
import com.wakati.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class CustomerTransactionLimitService {

    @Autowired
    private CustomerTransferLimitsRepository repository;
    @Autowired
    private UserRepository userRepository;

    // ===========================
    // GET LIMITS
    // ===========================
    public Map<String, Object> getLimits(String customerId) {

        CustomerTransferLimitProjection projection =
                repository.findLimitProjection(customerId)
                        .orElse(null);

        // ✅ GLOBAL DEFAULT
        if (customerId == null || customerId.isBlank()) {

            return Map.of(
                    "customer_id", null,
                    "customer_name", null,
                    "daily_transfer_limit",
                    projection != null ? projection.getDailyTransferLimit() : BigDecimal.ZERO,
                    "single_transfer_limit",
                    projection != null ? projection.getSingleTransferLimit() : BigDecimal.ZERO,
                    "limit_source", "global_default",
                    "set_by",
                    projection != null ? projection.getSetByUserId() : null
            );
        }

        // ✅ Validate customer exists
        User customer = userRepository.findByUserId(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        boolean isCustomerSpecific =
                projection != null && projection.getCustomerUserId() != null;

        return Map.of(
                "customer_id", customerId,
                "customer_name", customer.getFullName(),
                "daily_transfer_limit",
                projection != null ? projection.getDailyTransferLimit() : BigDecimal.ZERO,
                "single_transfer_limit",
                projection != null ? projection.getSingleTransferLimit() : BigDecimal.ZERO,
                "limit_source",
                isCustomerSpecific ? "customer_specific" : "global_default",
                "set_by",
                projection != null ? projection.getSetByUserId() : null
        );
    }

    // ===========================
    // SET LIMITS
    // ===========================
    public Map<String, Object> setLimits(TransactionLimitRequest request, String authUserId) {

        User caller = userRepository.findByUserId(authUserId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // ✅ Role check
        if (!List.of(UserType.ADMIN, UserType.CFO).contains(caller.getUserType())) {
            throw new RuntimeException("Access denied");
        }

        String customerId = (request.getCustomerId() == null || request.getCustomerId().isBlank())
                ? null
                : request.getCustomerId().trim();

        BigDecimal daily = request.getDailyTransferLimit();
        BigDecimal single = request.getSingleTransferLimit();

        if (daily == null && single == null) {
            throw new RuntimeException("Provide at least one limit");
        }

        if (daily != null && daily.compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("Invalid daily limit");
        }

        if (single != null && single.compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("Invalid single limit");
        }

        User customer = null;

        // ✅ Validate customer if present
        if (customerId != null) {
            customer = userRepository.findByUserId(customerId)
                    .orElseThrow(() -> new RuntimeException("Customer not found"));
        }

        // ✅ UPSERT
        Optional<CustomerTransferLimits> existingOpt =
                (customer != null)
                        ? repository.findByCustomer(customer)
                        : repository.findByCustomerIsNull();

        CustomerTransferLimits entity;
        User user = new User();
        user.setUserId(authUserId);
        if (existingOpt.isPresent()) {

            entity = existingOpt.get();

            entity.setDailyTransferLimit(
                    daily != null ? daily : entity.getDailyTransferLimit()
            );

            entity.setSingleTransferLimit(
                    single != null ? single : entity.getSingleTransferLimit()
            );


            entity.setSetBy(user);
            entity.setUpdatedAt(LocalDateTime.now());

        } else {

            entity = new CustomerTransferLimits();

            entity.setCustomer(customer); // null = global
            entity.setDailyTransferLimit(daily != null ? daily : BigDecimal.ZERO);
            entity.setSingleTransferLimit(single != null ? single : BigDecimal.ZERO);
            entity.setSetBy(user);
        }

        repository.save(entity);

        return Map.of(
                "customer_id", customerId,
                "scope", customerId != null ? "customer_specific" : "global_default",
                "daily_transfer_limit", entity.getDailyTransferLimit(),
                "single_transfer_limit", entity.getSingleTransferLimit(),
                "action", existingOpt.isPresent() ? "updated" : "created"
        );
    }
}