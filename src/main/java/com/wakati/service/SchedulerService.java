package com.wakati.service;

import com.wakati.entity.UserAttributes;
import com.wakati.model.response.UserWithExpiryProjection;
import com.wakati.repository.UserAttributesRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class SchedulerService {

    private static final Logger log = LoggerFactory.getLogger(SchedulerService.class);

    @Autowired
    private UserAttributesRepository userAttributesRepository;

    @Scheduled(cron = "0 0 10 * * ?")
    public void scheduleIdExpiryUsers() {

        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        log.info("=== Scheduler Started ===");

        List<UserWithExpiryProjection> users =
                userAttributesRepository.findAllCustomersWithIdExpiry();

        if (users==null || users.isEmpty()) {
            log.info("No users found for expiry check");
            return;
        }

        for (UserWithExpiryProjection user : users) {

            String expiry = user.getIdExpiry();

            if (expiry == null || expiry.isBlank()) {
                log.info("Skipping user {} due to empty expiry", user.getUserId());
                continue;
            }

            try {
                LocalDate expiryDate = LocalDate.parse(expiry.trim(), formatter);

                long daysLeft = ChronoUnit.DAYS.between(today, expiryDate);
                if (daysLeft >= 0 && daysLeft < 30) {

                    log.info(" Expiring Soon | User: {} | Name: {} | UserType: {} | Expiry: {} | Days Left: {}",
                            user.getUserId(),
                            user.getFullName(),
                            user.getUserType(),
                            expiryDate,
                            daysLeft
                    );

                    // call notificationService
                }

            } catch (Exception e) {
                log.error("Invalid date format for user {} with value {}",
                        user.getUserId(), expiry, e);
            }
        }

        log.info("=== Scheduler Completed ===");
    }
}


