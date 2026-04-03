package com.wakati.controller;

import com.wakati.model.request.SessionHistoryRequest;
import com.wakati.service.SessionHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/session-history")
public class SessionHistoryController {
    @Autowired
    private SessionHistoryService service;

    @PostMapping
    public ResponseEntity<Map<String, Object>> getSessionHistory(
            @RequestBody SessionHistoryRequest request) {

        return ResponseEntity.ok(service.getSessionHistory(request));
    }
}