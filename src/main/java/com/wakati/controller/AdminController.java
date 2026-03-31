package com.wakati.controller;

import com.wakati.model.request.UpdateUserStatusRequest;
import com.wakati.service.AdminService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AdminService service;

    @PostMapping("/update-status")
    public ResponseEntity<?> updateStatus(@RequestBody UpdateUserStatusRequest req,
                                          HttpServletRequest request) {
        return ResponseEntity.ok(service.updateUserStatus(req, request));
    }
}