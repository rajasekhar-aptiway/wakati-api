package com.wakati.service;

import com.wakati.I18NConstants;
import com.wakati.exception.WakatiException;
import com.wakati.model.response.ResponseBuilder;
import com.wakati.model.response.UserProjection;
import com.wakati.model.response.UserResponse;
import com.wakati.repository.UserRepository;
import com.wakati.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class GetUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private ResponseBuilder responseBuilder;

    public Map<String, Object> getUserByMobileNo(String mobileNo) {

        // ✅ Validation moved here
        if (mobileNo == null || mobileNo.isBlank()) {
            throw new WakatiException(I18NConstants.MOBILE_NO_REQUIRED);
        }

        List<UserProjection> rows = userRepository.findUserByMobileNo(mobileNo);

        if (rows.isEmpty()) {
            throw new WakatiException(I18NConstants.USER_NOT_FOUND);
        }

        UserProjection first = rows.get(0);

        // ✅ Wallet Balance
        Double walletBalance = walletRepository.findByOwner_UserId(first.getUserId())
                .map(w -> Double.parseDouble(w.getBalance()))
                .orElse(0.0);

        // ✅ Build Response
        UserResponse response = UserResponse.builder()
                .userId(first.getUserId())
                .walletId(first.getWalletId())
                .userType(first.getUserType())
                .fullName(first.getFullName())
                .mobileNo(first.getMobileNo())
                .email(first.getEmail())
                .status(first.getStatus())
                .createdBy(first.getCreatedBy())
                .createdAt(first.getCreatedAt())
                .registrationStage(first.getRegistrationStage())
                .verifiedByAdmin(first.getVerifiedByAdmin())
                .verifiedByAdjudicator(first.getVerifiedByAdjudicator())
                .build();

        // ✅ Aggregate
        for (UserProjection row : rows) {

            if (row.getAttributeKey() != null) {
                response.addAttribute(row);
            }

            if (row.getDocumentId() != null) {
                response.addDocument(row);
            }
        }

        // ✅ Suggested Action
        Map<String, Object> suggestedAction = null;

        if ("DEALER".equalsIgnoreCase(response.getUserType())
                && "CLOSED".equalsIgnoreCase(response.getStatus().name())
                && walletBalance > 0) {

            suggestedAction = Map.of(
                    "txn_type", "SECURITY_DEPOSIT_WITHDRAWAL",
                    "amount", walletBalance,
                    "target_user_id", response.getUserId(),
                    "message", "This dealer account is closed. Security deposit of "
                            + walletBalance + " is pending withdrawal."
            );
        }

        return responseBuilder.success(I18NConstants.PROFILE_FETCH_SUCCESS, "data", Map.of(
                "content",response
        ));
    }

    public Map<String, Object> getProfileStatus(String mobileNo) {

            // ✅ Validation
        if (mobileNo == null || mobileNo.isBlank()) {
           throw new WakatiException(I18NConstants.MOBILE_NO_REQUIRED);
        }

        List<UserProjection> rows = userRepository.findProfileStatusByMobile(mobileNo);

        // ✅ Not found
        if (rows.isEmpty()) {
            throw new WakatiException(I18NConstants.USER_NOT_FOUND);
        }

        UserProjection first = rows.get(0);

        // ✅ Registration Status Logic
        String registrationStatus =
                "CUSTOMER".equalsIgnoreCase(first.getUserType())
                        ? first.getRegistrationStage()
                        : "REGISTRATION_COMPLETED";

        // ✅ Build base response
        UserResponse response = UserResponse.builder()
                .userId(first.getUserId())
                .userType(first.getUserType())
                .walletId(first.getWalletId())
                .fullName(first.getFullName())
                .mobileNo(first.getMobileNo())
                .email(first.getEmail())
                .status(first.getStatus())
                .createdBy(first.getCreatedBy())
                .createdAt(first.getCreatedAt())
                .registrationStage(first.getRegistrationStage())
                .verifiedByAdmin(first.getVerifiedByAdmin())
                .verifiedByAdjudicator(first.getVerifiedByAdjudicator())
                .build();


        // ✅ Remove duplicate attributes
        for (UserProjection row : rows) {

            if (row.getAttributeKey() != null) {
                response.addAttribute(row);
            }

            if (row.getDocumentId() != null) {
                response.addDocument(row);
            }
        }

        // ✅ Final Response
        return responseBuilder.success(I18NConstants.PROFILE_FETCH_SUCCESS, "data", Map.of(
                "content",response
        ));
    }

}
