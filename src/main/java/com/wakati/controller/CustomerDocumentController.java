package com.wakati.controller;

import com.wakati.service.CustomerDocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.Map;

@RestController
@RequestMapping("/api/documents")
public class CustomerDocumentController {

    @Autowired
    private CustomerDocumentService service;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadDocuments(
            @RequestParam("user_id") String userId,
            @RequestParam(value = "document_number", required = false) String documentNumber,
            @RequestParam(value = "Pin", required = false) String pin,
            MultipartHttpServletRequest request
    ) {
        Map<String, MultipartFile> files = request.getFileMap();

        return ResponseEntity.ok(
                service.uploadDocuments(userId, documentNumber, pin, files)
        );
    }

}