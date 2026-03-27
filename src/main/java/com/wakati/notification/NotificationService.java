package com.wakati.notification;

import com.wakati.enums.Language;
import com.wakati.enums.NotificationType;
import org.springframework.stereotype.Service;

@Service
public interface NotificationService {
    void sendSms(String phoneNumber, String templateId, Language language, NotificationType type);
    void sendEmail(String email, String templateId, Language language, NotificationType type);
}
