package com.wakati.controller;

import com.wakati.model.request.IslandRegionRequest;
import com.wakati.service.IslandRegionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/islands-regions")
public class IslandRegionController {

    @Autowired
    private IslandRegionService service;

    @PostMapping
    public ResponseEntity<Map<String, Object>> handle(
            @RequestBody(required = false) IslandRegionRequest req) {

        return ResponseEntity.ok(service.handle(req));
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> fetch() {
        return ResponseEntity.ok(service.fetch());
    }
}