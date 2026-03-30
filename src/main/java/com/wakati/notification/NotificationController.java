package com.wakati.notification;

import com.wakati.enums.Language;
import com.wakati.enums.NotificationType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
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
    public ResponseEntity<String> sendEmail(
            @RequestParam String email,
            @RequestParam String templateId,
            @RequestParam Language language,
            @RequestParam NotificationType type
    ) {
        notificationService.sendEmail(email, templateId, language, type);
        return ResponseEntity.ok("Email sent successfully");
    }
}
