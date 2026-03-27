package com.wakati.notification;

import org.springframework.beans.factory.annotation.Value;
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

    public NotificationClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void sendSms(SmsProviderRequest request) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("API-KEY", apiKey);

        restTemplate.postForObject(BASE_URL + "/send", new HttpEntity<>(request, headers), String.class);
    }

    public void sendEmail(Object request) {
        restTemplate.postForObject(BASE_URL + "/send", request, String.class);
    }
}