package com.wakati.service;

import com.wakati.I18NConstants;
import com.wakati.config.UserContextHolder;
import com.wakati.entity.User;
import com.wakati.entity.UserCredentials;
import com.wakati.enums.Status;
import com.wakati.enums.UserType;
import com.wakati.exception.WakatiException;
import com.wakati.model.request.ChangePasswordRequest;
import com.wakati.model.response.ResponseBuilder;
import com.wakati.model.response.UserProjection;
import com.wakati.model.response.UserResponse;
import com.wakati.repository.UserCredentialsRepository;
import com.wakati.repository.UserRepository;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

import static com.wakati.I18NConstants.*;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
//    private final JwtService jwtService; // you must implement this

    @Autowired
    private ResponseBuilder responseBuilder;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserCredentialsRepository credentialsRepository;

    @Cacheable(value = "users", key = "#userId")
    public User getUserByUserId(String userId) {
        return userRepository.findByUserId(userId)
                .orElse(null);
    }

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


    @Transactional
    public Map<String, Object> changePassword(ChangePasswordRequest request) {

        String userId = request.getUserId();
        String password = request.getNewPassowrd();
        String oldPassword = request.getOldPassword();
        

        if (userId == null || password == null || password.isBlank()) {
            throw new WakatiException(INVALID_REQUEST);
        }

        if(password.equals(oldPassword)){
            throw new WakatiException(PASSWORDS_MUST_NOT_BE_SAME);
        }
        // ✅ Get authenticated user (from filter)
        User authUser = UserContextHolder.getUser();

        // 🔐 Prevent changing others' password
        if (!authUser.getUserId().equals(userId)) {
            throw new WakatiException(INVALID_REQUEST);
        }

        // ✅ Check user exists
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new WakatiException(USER_NOT_FOUND));

        // ✅ Upsert credentials
        UserCredentials cred = credentialsRepository
                .findByUser_UserId(userId)
                .orElseGet(() -> {
                    UserCredentials c = new UserCredentials();
                    c.setUser(user);
                    return c;
                });

        if (!passwordEncoder.matches(oldPassword, cred.getPassword())) {
            throw new WakatiException(INVALID_CURRENT_PASSWORD);
        }
        // ✅ Hash password
        String hash = passwordEncoder.encode(password);
        cred.setPassword(hash);
        cred.setPasswordAlgo("PASSWORD_BCRYPT");
        cred.setUpdatedAt(LocalDateTime.now());
        credentialsRepository.save(cred);

        //invalidate current session and force logout.
        user.setSessionToken(null);
        userRepository.save(user);

        return responseBuilder.success(I18NConstants.PASSWORD_UPDATE_SUCCESS,Map.of(
                "user_id", userId,
                "user_type", user.getUserType()
        ));

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

    @CacheEvict(value = "users", key = "#user.userId")
    public void updateUser(User user) {
        userRepository.save(user);
    }
}
