package com.wakati.controller;

import com.wakati.model.request.DTO.TransactionLimitRequest;
import com.wakati.service.CustomerTransactionLimitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transaction-limits")
public class CustomerTransactionLimitController {

    @Autowired
    private CustomerTransactionLimitService service;

    @PostMapping
    public ResponseEntity<?> handleLimits(
            @RequestBody TransactionLimitRequest request,
            @RequestHeader("userId") String authUserId
    ) {

        if ("get".equalsIgnoreCase(request.getAction())) {
            return ResponseEntity.ok(
                    service.getLimits(request.getCustomerId())
            );
        }

        if ("set".equalsIgnoreCase(request.getAction())) {
            return ResponseEntity.ok(
                    service.setLimits(request, authUserId)
            );
        }

        throw new RuntimeException("Invalid action");
    }
}