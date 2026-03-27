package com.wakati.notification;

import com.wakati.enums.Language;
import com.wakati.enums.NotificationType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class NotificationController {
    @Autowired
    private NotificationService notificationService;

    @PostMapping("/sms")
    public ResponseEntity<String> sendSms(
            @RequestParam String phoneNumber,
            @RequestParam String templateId,
            @RequestParam Language language,
            @RequestParam NotificationType type
    ) {

        notificationService.sendSms(phoneNumber, templateId, language, type);

        return ResponseEntity.ok("SMS Sent Successfully");
    }

    @PostMapping("/email")
    public ResponseEntity<String> sendEmail(@RequestBody EmailRequest request) {
        notificationService.sendEmail(
                request.getEmail(),
                request.getTemplateId(),
                request.getLanguage(),
                request.getType()
        );
        return ResponseEntity.ok("Email sent successfully");
    }
}
