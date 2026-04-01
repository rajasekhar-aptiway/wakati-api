package com.wakati.service;

import com.wakati.I18NConstants;
import com.wakati.entity.User;
import com.wakati.entity.UserCredentials;
import com.wakati.entity.Wallet;
import com.wakati.enums.OwnerType;
import com.wakati.enums.RegistrationStage;
import com.wakati.enums.Status;
import com.wakati.enums.WalletStatus;
import com.wakati.exception.WakatiException;
import com.wakati.model.request.PinRequest;
import com.wakati.model.response.ResponseBuilder;
import com.wakati.repository.UserCredentialsRepository;
import com.wakati.repository.UserRepository;
import com.wakati.repository.WalletRepository;
import com.wakati.util.PasswordUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Service
public class CustomerSecurePinService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserCredentialsRepository credentialsRepository;

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private ResponseBuilder responseBuilder;


    private void validateRequest(PinRequest request) {
        if (request.getUserId() == null || request.getPin() == null) {
            throw new WakatiException(I18NConstants.USER_ID_AND_PIN_REQUIRED);
        }
    }

    private User fetchUser(String userId) {
        return userRepository.findByUserId(userId)
                .orElseThrow(() -> new WakatiException(I18NConstants.USER_NOT_FOUND));
    }

    private UserCredentials getOrCreateCredentials(User user) {
        return credentialsRepository.findByUser_UserId(user.getUserId())
                .orElseGet(() -> {
                    UserCredentials c = new UserCredentials();
                    c.setUser(user);
                    c.setCreatedBy(user.getUserId());
                    return c;
                });
    }

    private String hashPin(String pin) {
        return passwordEncoder.encode(pin);
    }

    private void createOrActivateWallet(User user) {
        walletRepository.findByOwner_UserId(user.getUserId())
                .map(wallet -> {
                    // Wallet exists — update status if needed
                    if (wallet.getStatus() != WalletStatus.ACTIVE) {
                        wallet.setStatus(WalletStatus.ACTIVE);
                        return walletRepository.save(wallet);
                    }
                    return wallet;
                })
                .orElseGet(() -> {
                    // Wallet does not exist — create new
                    Wallet wallet = new Wallet();
                    wallet.setWalletId(user.getUserId());
                    wallet.setOwner(user);
                    wallet.setOwnerType(OwnerType.USER);
                    wallet.setBalance(BigDecimal.ZERO);
                    wallet.setStatus(WalletStatus.ACTIVE);
                    return walletRepository.save(wallet);
                });
    }

    private String generateAndSetPassword(UserCredentials credentials) {
        String password = PasswordUtil.generatePassword(6);
        credentials.setPassword(passwordEncoder.encode(password));
        credentials.setPasswordAlgo("PASSWORD_BCRYPT");
        credentials.setPasswordUpdatedAt(LocalDateTime.now().toString());
        return password;
    }


    /**
     * Set or update PIN + generate password + create or update wallet status
     */
    @Transactional
    public Map<String, Object> setPin(PinRequest request) {
        // 1. Validate input
        validateRequest(request);

        // 2. Fetch user
        User user = fetchUser(request.getUserId());

        //3.Hash pin
        String pinHash = hashPin(request.getPin());

        // 4. Get/Create Credentials
        UserCredentials credentials = getOrCreateCredentials(user);

        credentials.setPinHash(pinHash);
        credentials.setPinAlgo("PASSWORD_BCRYPT");

        // Generate and set password
        String password = generateAndSetPassword(credentials);
        credentialsRepository.save(credentials);

        // Update user
        user.setRegistrationStage(RegistrationStage.REGISTRATION_COMPLETED);
        user.setStatus(Status.PENDING);
        userRepository.save(user);

        // Create or activate wallet
        createOrActivateWallet(user);

        return responseBuilder.success(I18NConstants.PIN_SET_SUCCESSFULLY, "data", Map.of(
                "user_id", user.getUserId(),
                "generated_password", password,
                "sms_status", "fail"
        ));
    }

    /**
     * Set or update PIN only
     */
    @Transactional
    public Map<String, Object> updatePin(PinRequest request) {
        // 1. Validate input
        validateRequest(request);

        // 2. Fetch user
        User user = fetchUser(request.getUserId());
        //3.Hash pin
        String pinHash = hashPin(request.getPin());

        // 4. Get/Create Credentials
        UserCredentials credentials = getOrCreateCredentials(user);

        credentials.setPinHash(pinHash);
        credentials.setPinAlgo("PASSWORD_BCRYPT");

        credentialsRepository.save(credentials);

        return responseBuilder.success(I18NConstants.PIN_SET_SUCCESSFULLY, "data", Map.of(
                "user_id", user.getUserId(),
                "sms_status", "fail"
        ));
    }
}
