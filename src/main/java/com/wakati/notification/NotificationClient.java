package com.wakati.notification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

@Component
public class NotificationClient {

    private final RestTemplate restTemplate;

    @Value("${sms.api-key}")
    private static String apiKey;

    @Value("${bulk.sms.api-url}")
    private static String BASE_URL;

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String mailUsername;

    public NotificationClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void sendSms(SmsProviderRequest request) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("API-KEY", apiKey);

        restTemplate.postForObject(BASE_URL + "/send", new HttpEntity<>(request, headers), String.class);
    }

    public void sendEmail(String to, String subject, String content) throws  Exception{
//        restTemplate.postForObject(BASE_URL + "/send", request, String.class);
            SimpleMailMessage mail = new SimpleMailMessage();
            mail.setFrom(mailUsername);
            mail.setTo(to);
            mail.setSubject(subject);   // ✅ dynamic subject
            mail.setText(content);

            javaMailSender.send(mail);

    }
}