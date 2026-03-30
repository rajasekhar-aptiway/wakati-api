package com.wakati.notification;

import com.wakati.entity.EmailTemplate;
import com.wakati.enums.Language;
import com.wakati.enums.NotificationType;
import com.wakati.repository.EmailTemplatesRepository;
import com.wakati.repository.OtpChallengeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private NotificationClient notificationClient;

    @Autowired
    private OtpChallengeRepository otpChallengeRepository;

    @Autowired
    private EmailTemplatesRepository emailTemplatesRepository;

    @Override
    public void sendSms(String phoneNumber, String templateId, Language language, String content) {

        SmsProviderRequest request = new SmsProviderRequest();
        request.setPhoneNumber(phoneNumber);
        request.setContent(content);
        request.setFrom("+269111111");

        notificationClient.sendSms(request);
    }

    @Override
    public void sendEmail(String email,
                          String subject,
                          String content) throws Exception {
            notificationClient.sendEmail(email,subject,content);
    }

    private String buildMessage(NotificationType type,String templateId,String language,Object... args) {

        if (type == NotificationType.OTP) {
            String otp = String.valueOf((int)(Math.random() * 900000) + 100000);
            return "Your OTP is " + otp;
        }

        if (type == NotificationType.MESSAGE) {
            Optional<EmailTemplate> templateOpt = emailTemplatesRepository.findByTemplateKeyAndLanguage(templateId, language);
            if(templateOpt.isPresent()){
                EmailTemplate emailTemplate = templateOpt.get();
                String body = emailTemplate.getBody();
                if (args != null && args.length > 0) {
                    return MessageFormat.format(emailTemplate.getBody(), args);
                }
            }
            return "This is a notification message from wakati";
        }

        return "Default message";
    }

}
