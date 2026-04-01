package com.wakati.controller;

import com.wakati.model.request.PinRequest;
import com.wakati.service.CustomerSecurePinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api")
public class CustomerPinController {

    @Autowired
    private CustomerSecurePinService customerSecurePinService;

    @PostMapping("/set-pin")
    public ResponseEntity<?> setPin(@RequestBody PinRequest request) {
        return ResponseEntity.ok(customerSecurePinService.setPin(request));
    }

    @PostMapping("/update-pin")
    public ResponseEntity<?> updatePin(@RequestBody PinRequest request) {
        return ResponseEntity.ok(customerSecurePinService.updatePin(request));
    }
}
