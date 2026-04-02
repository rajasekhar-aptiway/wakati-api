package com.wakati.transaction.controller;

import com.wakati.transaction.model.TransactionRequest;
import com.wakati.transaction.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private TransactionService transactionService;

    @PostMapping
    public ResponseEntity<?> process(@RequestBody TransactionRequest request) {
        return ResponseEntity.ok(transactionService.process(request));
    }
}