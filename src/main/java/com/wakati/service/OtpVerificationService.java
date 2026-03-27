package com.wakati.service;

import com.wakati.entity.OtpChallenge;
import com.wakati.entity.SecurityEventLog;
import com.wakati.entity.User;
import com.wakati.enums.SecurityEventType;
import com.wakati.exception.WakatiException;
import com.wakati.model.request.OtpVerifyRequest;
import com.wakati.model.response.DTO.AttributeDTO;
import com.wakati.model.response.DTO.DocumentDTO;
import com.wakati.model.response.OtpVerifyResponse;
import com.wakati.model.response.ResponseBuilder;
import com.wakati.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import static com.wakati.I18NConstants.*;

@Service
public class OtpVerificationService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserAttributesRepository attributesRepository;

    @Autowired
    private UserDocumentsRepository documentsRepository;

    @Autowired
    private OtpChallengeRepository otpRepository;

    @Autowired
    private SecurityEventLogRepository logRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ResponseBuilder responseBuilder;

    @Transactional
    public Map<String, Object> verifyOtp(OtpVerifyRequest request) {

        String userId = request.getUserId();
        String otpInput = request.getOtp();

        if (userId == null || otpInput == null) {
            throw new WakatiException(USER_ID_AND_OTP_REQUIRED);
        }

        // ✅ Fetch User
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new WakatiException(HttpStatus.NO_CONTENT,USER_NOT_FOUND));

        // ✅ Fetch OTP
        OtpChallenge otp = otpRepository
                .findLatestOtp(userId, PageRequest.of(0, 1))
                .stream()
                .findFirst()
                .orElseThrow(() -> new WakatiException(HttpStatus.NO_CONTENT,NO_OTP_FOUND));

        boolean isDev = "development".equals(System.getenv("APP_ENV"));
        boolean isMasterOtp = isDev && "1111".equals(otpInput);

        // ✅ Already verified
        if (!isMasterOtp && Boolean.TRUE.toString().equalsIgnoreCase(otp.getVerified())) {
            throw new WakatiException(OTP_ALREADY_VERIFIED);
        }

        // ✅ Expiry check (10 min)
        if (!isMasterOtp) {
            long age = Duration.between(otp.getCreatedAt(), LocalDateTime.now()).getSeconds();
            if (age > 600) {
                throw new WakatiException(OTP_EXPIRED);
            }
        }

        // ✅ Verify OTP
        if (!isMasterOtp && !passwordEncoder.matches(otpInput, otp.getOtpHash())) {
            logEvent(SecurityEventType.OTP_FAIL, userId);
            throw new WakatiException(INVALID_OTP);
        }

        // ✅ Mark verified
        otp.setVerified(Boolean.TRUE.toString());
        otpRepository.save(otp);

        logEvent(SecurityEventType.OTP_SUCCESS, userId);

        // ✅ Fetch attributes
        List<AttributeDTO> attributes = attributesRepository.findByUserId(userId)
                .stream()
                .map(a -> new AttributeDTO(
                        a.getAttributeId(),
                        a.getAttributeKey(),
                        a.getAttributeValue()
                ))
                .toList();

        // ✅ Fetch documents
        List<DocumentDTO> documents = documentsRepository.findByUserId(userId)
                .stream()
                .map(d -> new DocumentDTO(
                        d.getDocumentId(),
                        d.getDocumentType().name(),
                        d.getDocumentNumber(),
                        "kyc_document?id=" + d.getDocumentId(),
                        d.getVerificationStatus()
                ))
                .toList();

        // ✅ Response
        return responseBuilder.success(OTP_VERIFIED_SUCCESSFULLY,buildResponse(user, attributes, documents));
    }

    private void logEvent(SecurityEventType type, String userId) {
        SecurityEventLog log = new SecurityEventLog();
        log.setEventType(type);
        User user = new User();
        user.setUserId(userId);
        log.setUser(user);
        log.setCreatedAt(LocalDateTime.now());
        logRepository.save(log);
    }

    private OtpVerifyResponse buildResponse(User user,
                                            List<AttributeDTO> attributes,
                                            List<DocumentDTO> documents) {

        OtpVerifyResponse res = new OtpVerifyResponse();
        res.setUserId(user.getUserId());
        res.setFullName(user.getFullName());
        res.setMobileNo(user.getMobileNo());
        res.setEmail(user.getEmail());
        res.setStatus(user.getStatus());
        res.setUserType(user.getUserType());
        res.setCreatedBy(user.getCreatedBy());
        res.setCreatedAt(user.getCreatedAt());
        res.setAttributes(attributes);
        res.setDocuments(documents);

        return res;
    }
}