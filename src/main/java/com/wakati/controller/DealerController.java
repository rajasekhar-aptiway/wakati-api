package com.wakati.controller;

import com.wakati.model.request.DealerRequest;
import com.wakati.service.DealerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class DealerController {

    @Autowired
    private DealerService dealerService;

    @PostMapping("/dealer_list")
    public ResponseEntity<?> getDealers(@RequestBody DealerRequest request) {
        return ResponseEntity.ok(dealerService.getDealers(request));
    }
}