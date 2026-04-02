package com.wakati.service;

import com.wakati.I18NConstants;
import com.wakati.config.UserContextHolder;
import com.wakati.entity.User;
import com.wakati.entity.Wallet;
import com.wakati.enums.TransactionType;
import com.wakati.exception.WakatiException;
import com.wakati.model.response.ResponseBuilder;
import com.wakati.model.response.UserProjection;
import com.wakati.model.response.UserResponse;
import com.wakati.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class DashboardService {

    @Autowired private UserRepository userRepository;
    @Autowired private WalletRepository walletRepository;
    @Autowired private CashRepository cashRepository;
    @Autowired private TransactionRepository transactionRepository;
    @Autowired private NotificationRepository notificationRepository;
    @Autowired private ResponseBuilder responseBuilder;

    public Map<String, Object> getDashboard(String userId) {

        // 🔐 Auth user from JWT
        User authUser = UserContextHolder.getUser();

        // ✅ IDOR check
        if (!userId.equals(authUser.getUserId())) {
            List<String> allowed = List.of(
                    "ADMIN","ADJUDICATOR","CFO","RECEIVER",
                    "SUPER_DEALER","DEALER","PARTNER_AGENT","FRONT_DESK"
            );

            if (!allowed.contains(authUser.getUserType().name())) {
                throw new WakatiException(I18NConstants.ACCESS_DENIED);
            }
        }

        // ✅ Fetch user data
        List<UserProjection> rows = userRepository.findDashboardData(userId);

        if (rows.isEmpty()) {
            throw new WakatiException(I18NConstants.USER_NOT_FOUND);
        }

        UserProjection first = rows.get(0);

        // ✅ Build response
        UserResponse response = UserResponse.builder()
                .userId(first.getUserId())
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

        // To avoid duplicates
        Set<String> attributeIds = new HashSet<>();
        Set<String> documentIds = new HashSet<>();

        // ✅ Merge attributes & documents
        for (UserProjection row : rows) {

            // ✅ Attribute handling
            if (row.getAttributeId() != null && attributeIds.add(row.getAttributeId())) {
                response.addAttribute(row);
            }

            // ✅ Document handling (fixes your crash)
            if (row.getDocumentId() != null && documentIds.add(row.getDocumentId())) {
                response.addDocument(row);
            }
        }

        String userType = first.getUserType();

        // ✅ Wallet
        BigDecimal walletBalance = walletRepository.findByOwner_UserId(userId)
                .map(Wallet::getBalance)
                .orElse(BigDecimal.ZERO);

        // ✅ Cash
        Double cash = cashRepository.getCashBalance(userId);
        double cashBalance = (cash != null) ? cash : 0.0;

        // ✅ Today metrics
        double deposit = Optional.ofNullable(
                transactionRepository.getTodayAmount( TransactionType.CASH_DEPOSIT,userId)
        ).orElse(0.0);

        double withdraw = Optional.ofNullable(
                transactionRepository.getTodayAmount(TransactionType.CASH_WITHDRAWAL,userId)
        ).orElse(0.0);
        double commission = transactionRepository.getTodayCommission(userId);

        // ✅ Notifications
        long notifCount = notificationRepository.countRunning(userId);

        // ✅ Profile Data (walletData)
        Map<String, Object> profileData = new HashMap<>();
        profileData.put("user_id", userId);
        profileData.put("userType", userType);
        profileData.put("todayDeposit", deposit);
        profileData.put("todayWithdraw", withdraw);
        profileData.put("todayCommission", commission);
        profileData.put("runningNotificationCount", notifCount);

        // 🔥 Role-based logic
        if ("CUSTOMER".equals(userType) || "INSTITUTIONAL_USER".equals(userType)) {

            profileData.put("walletBalance", walletBalance);

        } else {

            profileData.put("cashBalance", cashBalance);
            profileData.put("walletBalance", walletBalance);

            if ("DEALER".equals(userType)) {
                profileData.put("openingBalance", 0); // TODO repo
                profileData.put("securityDeposit", 0);
            }

            if ("SUPER_DEALER".equals(userType)) {
                profileData.put("todaySourceDistributions", 0);
                profileData.put("todaySourceCollection", 0);
                profileData.put("todayDealersCollection", 0);
                profileData.put("todayDealersDistributions", 0);
            }

            if ("RECEIVER".equals(userType)) {
                profileData.put("platformBalance", cashRepository.getPlatformBalance());
            }
        }

        return responseBuilder.success(I18NConstants.DASHBOARD_DATA_SUCCESS, "data", Map.of(
                "walletData", profileData,
                "prfileData",response
        ));
    }
}
