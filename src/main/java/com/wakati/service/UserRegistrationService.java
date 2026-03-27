package com.wakati.service;

import com.wakati.entity.OtpChallenge;
import com.wakati.entity.User;
import com.wakati.entity.UserAttributes;
import com.wakati.entity.Wallet;
import com.wakati.enums.*;
import com.wakati.model.request.UserRegistrationRequest;
import com.wakati.repository.OtpChallengeRepository;
import com.wakati.repository.UserAttributesRepository;
import com.wakati.repository.UserRepository;
import com.wakati.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public Map<String, Object> register(UserRegistrationRequest request) {

        // ✅ 1. Duplicate Mobile Check
        userRepository.findTopByMobileNoAndStatusNotOrderByCreatedAtDesc(
                request.getMobileNo(), Status.REJECTED
        ).ifPresent(u -> {
            throw new RuntimeException("Mobile already registered");
        });

        // ✅ 2. Duplicate Email Check
        userRepository.findTopByEmailAndStatusNotOrderByCreatedAtDesc(
                request.getEmail(), Status.REJECTED
        ).ifPresent(u -> {
            throw new RuntimeException("Email already registered");
        });

        // ✅ 3. ID number check
        if (request.getIdNumber() != null) {
            long count = attributesRepository.countByIdNumber(request.getIdNumber());
            if (count >= 2) {
                throw new RuntimeException("ID number already used by 2 users");
            }
        }

        // ✅ 4. Create User
        String userId = UUID.randomUUID().toString();

        User user = new User();
        user.setUserId(userId);
        user.setUserType(request.getUserType());
        user.setMobileNo(request.getMobileNo());
        user.setEmail(request.getEmail());
        user.setFullName(request.getFullName());
        user.setIsland(request.getIsland());
        user.setStatus(Status.DRAFT);
        user.setRegistrationStage(RegistrationStage.MOBILE_VERIFICATION_PENDING);
        user.setCreatedBy(userId);

        userRepository.save(user);

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

        // ✅ Response
        return Map.of(
                "code", 200,
                "message", "User registered successfully",
                "data", Map.of(
                        "userId", userId,
                        "mobile", request.getMobileNo(),
                        "email", request.getEmail()
                )
        );
    }
}