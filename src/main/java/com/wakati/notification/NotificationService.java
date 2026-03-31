package com.wakati.notification;

import com.wakati.enums.Language;
import com.wakati.enums.NotificationType;
import org.springframework.stereotype.Service;

@Service
public interface NotificationService {
    void sendSms(String phoneNumber, String content);
    void sendEmail(String email, String subject,String content) throws Exception ;
}
