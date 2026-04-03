package com.wakati.controller;

import com.wakati.model.request.DisputeRequest;
import com.wakati.service.DisputeService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/disputes")
public class DisputeController {

    @Autowired
    private DisputeService service;

    @PostMapping
    public ResponseEntity<Map<String, Object>> handle(@RequestBody DisputeRequest req, HttpServletRequest httpRequest) {
        return ResponseEntity.ok(service.handle(req,httpRequest));
    }
}