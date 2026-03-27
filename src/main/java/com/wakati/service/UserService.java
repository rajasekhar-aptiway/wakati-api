package com.wakati.service;

import com.wakati.I18NConstants;
import com.wakati.enums.Status;
import com.wakati.enums.UserType;
import com.wakati.model.response.ResponseBuilder;
import com.wakati.model.response.UserProjection;
import com.wakati.model.response.UserResponse;
import com.wakati.repository.UserRepository;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
//    private final JwtService jwtService; // you must implement this

    @Autowired
    private ResponseBuilder responseBuilder;

    public Map<String, Object> getUsers(Status status, List<UserType> type) {

/**       Long authUserId = jwtService.extractUserId(token);

 String role = userRepository.findUserTypeByUserId(authUserId);
 if (role == null || !List.of("ADMIN", "ADJUDICATOR", "CFO").contains(role.toUpperCase())) {
 throw new RuntimeException("Access Denied");
 }
 **/


        List<UserProjection> rows = userRepository.fetchUsers(status, type);

        return getUserResponse(rows);
    }

    private @NonNull Map<String, Object> getUserResponse(List<UserProjection> rows) {
        List<Object[]> objects = userRepository.countByStatusGrouped();

        Map<Status, Long> counts = new EnumMap<>(Status.class);

        for (Object[] row : userRepository.countByStatusGrouped()) {
            counts.put((Status) row[0], (Long) row[1]);
        }
        // ✅ COUNTS
        int pending = counts.getOrDefault(Status.PENDING, 0L).intValue();
        int approved = counts.getOrDefault(Status.APPROVED, 0L).intValue();
        int rejected = counts.getOrDefault(Status.REJECTED, 0L).intValue();

        // ✅ BUILD RESPONSE (DEDUP)
        Map<String, UserResponse> usersMap = new LinkedHashMap<>();

        for (UserProjection row : rows) {
            usersMap.putIfAbsent(row.getUserId(), buildBaseUser(row));
            UserResponse user = usersMap.get(row.getUserId());

            // CUSTOMER EXTRA FIELDS
            if ("CUSTOMER".equals(row.getUserType())) {
                user.setRegistrationStage(row.getRegistrationStage());
                user.setVerifiedByAdmin(row.getVerifiedByAdmin());
                user.setVerifiedByAdjudicator(row.getVerifiedByAdjudicator());
            }

            // ATTRIBUTES
            if (row.getAttributeKey() != null) {
                user.addAttribute(row);
            }

            // DOCUMENTS
            if (row.getDocumentId() != null) {
                user.addDocument(row);
            }
        }


        return responseBuilder.success(I18NConstants.CUSTOMER_LIST_FETCHED_SUCCESSFULLY, "data", Map.of(
                "pendingcount", pending,
                "approvedcount", approved,
                "rejectedcount", rejected,
                "content", usersMap.values()
        ));
    }

    private Status mapStatus(String status) {
        if (status == null) return null;

        return switch (status.toLowerCase()) {
            case "pending" -> Status.PENDING;
            case "approved" -> Status.APPROVED;
            case "rejected" -> Status.REJECTED;
            default -> null;
        };
    }

    private UserResponse buildBaseUser(UserProjection row) {
        return UserResponse.builder()
                .userId(row.getUserId())
                .walletId(row.getWalletId())
                .userType(row.getUserType())
                .fullName(row.getFullName())
                .mobileNo(row.getMobileNo())
                .email(row.getEmail())
                .status(row.getStatus())
                .createdBy(row.getCreatedBy())
                .createdAt(row.getCreatedAt())
                .attributes(new ArrayList<>())
                .documents(new ArrayList<>())
                .build();
    }


    public Map<String, Object> getUsersList(int size, int offset) {

        if (size <= 0) {
            size = 20;
        }
        if (offset <= 1) {
            offset = 1;
        }

        List<String> pageIds = userRepository.findUserIds(size, offset);
        List<UserProjection> userProjections = userRepository.fetchUsersByIds(pageIds);
        return getUserResponse(userProjections);
    }
}
