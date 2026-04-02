package com.wakati.controller;

import com.wakati.model.request.AccountStatementRequest;
import com.wakati.service.AccountStatementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/account")
public class AccountStatementController {

    @Autowired
    private AccountStatementService service;

    @PostMapping("/statement")
    public ResponseEntity<?> getStatement(@RequestBody AccountStatementRequest req) {
        return ResponseEntity.ok(service.getStatement(req));
    }
}