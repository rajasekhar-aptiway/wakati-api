package com.wakati.service;

import com.wakati.I18NConstants;
import com.wakati.entity.*;
import com.wakati.enums.*;
import com.wakati.exception.WakatiException;
import com.wakati.model.request.UserRegistrationRequest;
import com.wakati.model.response.ResponseBuilder;
import com.wakati.notification.NotificationService;
import com.wakati.repository.*;
import com.wakati.util.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class UserRegistrationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserAttributesRepository attributesRepository;

    @Autowired
    private OtpChallengeRepository otpRepository;

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private ResponseBuilder responseBuilder;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private UserCredentialsRepository userCredentialsRepository;

    @Autowired
    private EmailTemplatesRepository emailTemplatesRepository;

    @Transactional
    public Map<String, Object> register(UserRegistrationRequest request) {

        // ✅ 1. Duplicate Mobile Check
        userRepository.findTopByMobileNoAndStatusNotOrderByCreatedAtDesc(
                request.getMobileNo(), Status.REJECTED
        ).ifPresent(u -> {
            throw new WakatiException(I18NConstants.MOBILE_ALREADY_REGISTERED);
        });

        // ✅ 2. Duplicate Email Check
        userRepository.findTopByEmailAndStatusNotOrderByCreatedAtDesc(
                request.getEmail(), Status.REJECTED
        ).ifPresent(u -> {
            throw new WakatiException(I18NConstants.EMAIL_ALREADY_REGISTERED);
        });

        // ✅ 3. ID number check
        if (request.getIdNumber() != null) {
            long count = attributesRepository.countByIdNumber(request.getIdNumber());
            if (count >= 2) {
                throw new WakatiException(I18NConstants.ID_NUMBER_ALREADY_USED_BY_2_USERS);
            }
        }

        // ✅ 4. Create User
        String userId = UUID.randomUUID().toString();

        User user = new User();
        user.setUserId(userId);
        user.setUserType(request.getUserType());
        user.setMobileNo("269"+request.getMobileNo());
        user.setEmail(request.getEmail());
        user.setFullName(request.getFullName());
        user.setIsland(request.getIsland());
        user.setStatus(Status.DRAFT);
        user.setRegistrationStage(RegistrationStage.MOBILE_VERIFICATION_PENDING);
        user.setCreatedBy(userId);

        userRepository.save(user);

        UserCredentials credentials = new UserCredentials();
        credentials.setUser(user);
        String defaultPassword = PasswordUtil.generatePassword(8);
        credentials.setPassword(passwordEncoder.encode(defaultPassword));
        credentials.setPasswordAlgo("PASSWORD_BCRYPT");
        credentials.setCreatedBy(user.getUserId());
        credentials.setCreatedAt(LocalDateTime.now());
        userCredentialsRepository.save(credentials);

        Optional<EmailTemplate> templateOpt = emailTemplatesRepository.findByTemplateKeyAndLanguage("ACCOUNT_CREATED", user.getPreferredLanguage());
        if(templateOpt.isPresent()){
            EmailTemplate emailTemplate = templateOpt.get();
            String body = emailTemplate.getBody();
            body = body.replace("{{password}}",defaultPassword);
            try {
                notificationService.sendEmail(user.getEmail(), "ACCOUNT_APPROVED", body);
            }catch (Exception e){
                return responseBuilder.error(HttpStatus.EXPECTATION_FAILED,I18NConstants.ERROR_WHILE_SENDING_EMAIL);
            }
        }

        // ✅ 5. Attributes
        if (request.getAttributes() != null) {
            List<UserAttributes> attrList = new ArrayList<UserAttributes>();
            for (Map.Entry<String, String> entry : request.getAttributes().entrySet()) {
                UserAttributes attr = new UserAttributes();
                attr.setAttributeId(UUID.randomUUID().toString());
                attr.setUser(user);
                attr.setAttributeKey(entry.getKey());
                attr.setAttributeValue(entry.getValue());
                attr.setCreatedBy(userId);
                attrList.add(attr);
            }
            attributesRepository.saveAll(attrList);
        }

        // ID number separately
        if (request.getIdNumber() != null) {
            UserAttributes attr = new UserAttributes();
            attr.setAttributeId(UUID.randomUUID().toString());
            attr.setUser(user);
            attr.setAttributeKey("Idnumber");
            attr.setAttributeValue(request.getIdNumber());
            attr.setCreatedBy(userId);

            attributesRepository.save(attr);
        }

        // ✅ 6. OTP
        String otp = String.format("%04d", new Random().nextInt(10000));

        OtpChallenge otpEntity = new OtpChallenge();
        otpEntity.setOtpId(UUID.randomUUID().toString());
        otpEntity.setOtp(otp);
        otpEntity.setUser(user);
        otpEntity.setPurpose(OtpPurpose.REGISTER);

        otpRepository.save(otpEntity);

        // ✅ 7. Wallet
        Wallet wallet = new Wallet();
        wallet.setWalletId(UUID.randomUUID().toString());
        wallet.setOwnerType(OwnerType.USER);
        wallet.setOwner(user);
        wallet.setBalance("0");
        wallet.setStatus(WalletStatus.BLOCKED);

        walletRepository.save(wallet);

       return responseBuilder.success(I18NConstants.USER_REGISTERED_SUCCESSFULLY,"data", Map.of(
                "userId", userId,
                "mobile", request.getMobileNo(),
                "email", request.getEmail()
        ));

    }
}