package com.wakati.controller;

import com.wakati.model.request.LogViewerRequest;
import com.wakati.service.LogViewerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/logs")
public class LogViewerController {

    @Autowired
    private LogViewerService service;

    @PostMapping
    public ResponseEntity<Map<String, Object>> handle(@RequestBody LogViewerRequest req) {
        return ResponseEntity.ok(service.handle(req));
    }
}