package com.wakati.controller;

import com.wakati.model.request.AuditReportRequest;
import com.wakati.service.AuditReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reports")
public class AuditReportController {

    @Autowired
    private AuditReportService service;

    @PostMapping
    public ResponseEntity<?> getReport(@RequestBody AuditReportRequest request) {
        return ResponseEntity.ok(service.generateReport(request));
    }
}