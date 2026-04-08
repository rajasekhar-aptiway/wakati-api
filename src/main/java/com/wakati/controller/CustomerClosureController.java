package com.wakati.controller;

import com.wakati.service.CustomerClosureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/closure")
public class CustomerClosureController {

    @Autowired
    private  CustomerClosureService closureService;

    @PostMapping("/request")
    public ResponseEntity<?> requestClosure(
            @RequestParam(required = false) String customerId,
            @RequestHeader("userId") String authUserId
    ) {
        return ResponseEntity.ok(
                closureService.createClosureRequest(customerId, authUserId)
        );
    }

    @PostMapping("/update-status")
    public Map<String, Object> getCustomerClosure(
            @RequestParam("closureRequestId") String closureRequestId,
            @RequestParam("action") String action,
            @RequestParam(value = "remarks", required = false) String remarks,
            @RequestParam(value = "authUserId") String authUserId
    ){
        return closureService.processClosure(closureRequestId,action,remarks,authUserId);
    }

}