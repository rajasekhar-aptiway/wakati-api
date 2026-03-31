package com.wakati.service;

import com.wakati.I18NConstants;
import com.wakati.config.UserContextHolder;
import com.wakati.config.UserStatusEvent;
import com.wakati.entity.*;
import com.wakati.enums.*;
import com.wakati.exception.WakatiException;
import com.wakati.model.request.UpdateUserStatusRequest;
import com.wakati.model.response.ResponseBuilder;
import com.wakati.notification.NotificationService;
import com.wakati.repository.*;
import com.wakati.util.PasswordUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class AdminService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserDocumentsRepository documentsRepository;
    @Autowired
    private WalletRepository walletRepository;
    @Autowired
    private UserCredentialsRepository credentialsRepository;
    @Autowired
    private AdminActionLogRepository logRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private SmsTemplateRepository smsTemplateRepository;
    @Autowired
    private EmailTemplatesRepository emailTemplatesRepository;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private ResponseBuilder responseBuilder;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    private final static Logger log = LoggerFactory.getLogger(AdminService.class);

    @Transactional
    public Map<String, Object> updateUserStatus(UpdateUserStatusRequest request,
                                                HttpServletRequest http) {

        String userId = request.getUserId();
        Status status = request.getStatus();

        if (userId == null || status == null) {
            throw new WakatiException(I18NConstants.INVALID_REQUEST);
        }

        // ✅ Fetch user
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new WakatiException(I18NConstants.USER_NOT_FOUND));

        // ✅ Update USERS
        user.setStatus(status);

        if (status == Status.BLOCKED ||
            status == Status.SUSPENDED ||
            status == Status.REJECTED) {

            user.setSessionToken(null); // 🔥 invalidate JWT
        }

        userRepository.save(user);

        // ✅ Update USER_DOCUMENTS
        documentsRepository.updateStatusByUserId(userId, VerificationStatus.valueOf(status.name()));
        applicationEventPublisher.publishEvent(new UserStatusEvent(userId, status));
        // ✅ Audit log
        User admin = UserContextHolder.getUser();

        AdminActionLog adminActionLog = new AdminActionLog();
        adminActionLog.setActionType(ActionType.USER_STATUS_CHANGE);
        adminActionLog.setPerformedBy(admin != null ? admin.getUserId() : "SYSTEM");
        adminActionLog.setTargetUserId(userId);
        adminActionLog.setIpAddress(http.getRemoteAddr());
        adminActionLog.setCreatedAt(LocalDateTime.now());

        logRepository.save(adminActionLog);

        return Map.of(
                "code", 200,
                "message", "Status updated successfully",
                "data", Map.of(
                        "user_id", userId,
                        "status", status
                )
        );
    }

    // 🔥 POST-COMMIT OPERATIONS
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handlePostStatusUpdate(UserStatusEvent event) {

        String userId = event.getUserId();
        Status status = event.getStatus();

        if (status == Status.APPROVED) {

            // ✅ Wallet
            Wallet wallet = walletRepository.findByOwnerId(userId)
                    .orElse(null);

            if (wallet != null) {
                wallet.setStatus(WalletStatus.ACTIVE);
                walletRepository.save(wallet);
            } else {
                Wallet w = new Wallet();
                w.setWalletId(UUID.randomUUID().toString());
                User user = new User();
                user.setUserId(userId);
                w.setOwner(user);
                w.setOwnerType(OwnerType.USER);
                w.setBalance("0");
                w.setStatus(WalletStatus.ACTIVE);
                walletRepository.save(w);
            }

            // ✅ Credentials
            if (!credentialsRepository.existsByUser_UserId(userId)) {

                String password = PasswordUtil.generatePassword(8);
                String hash = passwordEncoder.encode(password);

                UserCredentials cred = new UserCredentials();
                User user = new User();
                user.setUserId(userId);
                cred.setUser(user);
                cred.setPassword(hash);
                cred.setPasswordAlgo("PASSWORD_BCRYPT");

                credentialsRepository.save(cred);

                // TODO: send SMS with password
                Optional<EmailTemplate> templateOpt = emailTemplatesRepository.findByTemplateKeyAndLanguage("ACCOUNT_CREATED", user.getPreferredLanguage());
                if(templateOpt.isPresent()){
                    EmailTemplate emailTemplate = templateOpt.get();
                    String body = emailTemplate.getBody();
                    body = body.replace("{{password}}",password);
                    try {
                        notificationService.sendEmail(user.getEmail(), "ACCOUNT_APPROVED", body);
                    }catch (Exception e){
                        log.error("Error while sending email to "+user.getEmail(),e.getMessage());
                        //return responseBuilder.error(HttpStatus.EXPECTATION_FAILED,I18NConstants.ERROR_WHILE_SENDING_EMAIL);
                    }
                }

                Optional<SmsTemplate> smsTemplateOpt = smsTemplateRepository.findByTemplateKeyAndLanguage("ACCOUNT_CREATED", Language.valueOf(user.getPreferredLanguage()));
                if(smsTemplateOpt.isPresent()){
                    SmsTemplate emailTemplate = smsTemplateOpt.get();
                    String body = emailTemplate.getBody();
                    body = body.replace("{{password}}",password);
                    try {
                        notificationService.sendSms(user.getMobileNo(), body);
                    }catch (Exception e){
                        log.error("Error while sending sms to "+user.getMobileNo(),e.getMessage());
                        //return responseBuilder.error(HttpStatus.EXPECTATION_FAILED,I18NConstants.ERROR_WHILE_SENDING_SMS);
                    }
                }
            }

        } else if (status == Status.BLOCKED ||
                   status == Status.REJECTED ||
                   status == Status.SUSPENDED) {

            walletRepository.updateStatusByOwnerId(userId, AccountStatus.BLOCKED);
        }
    }
}