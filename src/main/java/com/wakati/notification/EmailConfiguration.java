package com.wakati.notification;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class EmailConfiguration {

    @Value("${spring.mail.host}")
    private String mailHost;

    @Value("${spring.mail.port}")
    private String mailPort;

    @Value("${spring.mail.username}")
    private String mailUsername;

    @Value("${spring.mail.password}")
    private String mailPassword;

    @Bean
    public JavaMailSender javaMailSender(){
        JavaMailSenderImpl javaMailSenderimpl = new JavaMailSenderImpl();
        javaMailSenderimpl.setHost(mailHost);
        javaMailSenderimpl.setPort(Integer.parseInt(mailPort));
        javaMailSenderimpl.setUsername(mailUsername);
        javaMailSenderimpl.setPassword(mailPassword);

        Properties properties = javaMailSenderimpl.getJavaMailProperties();
        properties.put("mail.smtp.starttls.enable","true");
        return javaMailSenderimpl;

    }
}