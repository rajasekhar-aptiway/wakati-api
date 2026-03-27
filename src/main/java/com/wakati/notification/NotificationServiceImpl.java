package com.wakati.notification;

import com.wakati.enums.Language;
import com.wakati.enums.NotificationType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private NotificationClient notificationClient;


    @Override
    public void sendSms(String phoneNumber, String templateId, Language language, NotificationType type) {

        String message = buildMessage(type);

        SmsProviderRequest request = new SmsProviderRequest();
        request.setPhoneNumber(phoneNumber);
        request.setContent(message);
        request.setFrom("+269111111");

        notificationClient.sendSms(request);
    }

    @Override
    public void sendEmail(String email, String templateId, Language language, NotificationType type) {

        String message = buildMessage(type);

        EmailRequest request = new EmailRequest();
        request.setEmail(email);
        request.setTemplateId(templateId);
        request.setLanguage(language);
        request.setType(type);

        notificationClient.sendEmail(request);

    }

    private String buildMessage(NotificationType type) {

        if (type == NotificationType.OTP) {
            String otp = String.valueOf((int)(Math.random() * 900000) + 100000);
            return "Your OTP is " + otp;
        }

        if (type == NotificationType.MESSAGE) {
            return "This is a notification message";
        }

        return "Default message";
    }
}
