package com.wakati.notification;

import com.wakati.enums.Language;
import com.wakati.enums.NotificationType;
import com.wakati.repository.OtpChallengeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private NotificationClient notificationClient;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private OtpChallengeRepository otpChallengeRepository;

    @Value("${spring.mail.username}")
    private String mailUsername;

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
    public void sendEmail(String email,
                          String templateId,
                          Language language,
                          NotificationType type) {

        String message = buildMessage(type);

        // ✅ Build request (for external service)
        EmailRequest request = new EmailRequest();
        request.setFrom(mailUsername);
        request.setEmail(email);
        request.setTemplateId(templateId);
        request.setLanguage(language);
        request.setType(type);
        request.setBody(message);

        // =====================================================
        // ✅ PRIMARY: SMTP (JavaMailSender)
        // =====================================================
        try {
            SimpleMailMessage mail = new SimpleMailMessage();
            mail.setFrom(mailUsername);
            mail.setTo(email);
            mail.setSubject("WAKATI NOTIFACTION");   // ✅ dynamic subject
            mail.setText(message);

            javaMailSender.send(mail);

        } catch (Exception e) {
            // 🔥 fallback to external service
            System.err.println("SMTP failed, using fallback: " + e.getMessage());

            notificationClient.sendEmail(request);

        }
    }

    private String buildMessage(NotificationType type) {

        if (type == NotificationType.OTP) {
            String otp = String.valueOf((int)(Math.random() * 900000) + 100000);
            return "Your OTP is " + otp;
        }

        if (type == NotificationType.MESSAGE) {
            return "This is a notification message from wakati";
        }

        return "Default message";
    }

}
