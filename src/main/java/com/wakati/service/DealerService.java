package com.wakati.service;

import com.wakati.enums.Status;
import com.wakati.model.request.DealerRequest;
import com.wakati.model.response.UserBasicProjection;
import com.wakati.model.response.UserProjection;
import com.wakati.model.response.UserResponse;
import com.wakati.repository.DealerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DealerService {

    @Autowired
    private DealerRepository dealerRepository;


    public Map<String, Object> getDealers(DealerRequest request) {

        String platform = request.getPlatform();
        String statusParam = String.valueOf(request.getStatus());
        String userId = request.getUserId();

        // ✅ VALIDATION
        if (!"web".equalsIgnoreCase(platform) && userId == null) {
            throw new RuntimeException("user_id is required");
        }

        String status = mapStatus(statusParam);

        // =====================================================
        // ✅ WEB FLOW
        // =====================================================
        if ("web".equalsIgnoreCase(platform)) {

            List<UserProjection> rows = dealerRepository.fetchWebDealers(status);


            Map<Status, Long> counts = new EnumMap<>(Status.class);

            for (Object[] row : dealerRepository.countByStatusGrouped()) {
                counts.put((Status) row[0], (Long) row[1]);
            }
            int pending = counts.getOrDefault(Status.PENDING, 0L).intValue();
            int approved = counts.getOrDefault(Status.APPROVED, 0L).intValue();
            int rejected = counts.getOrDefault(Status.REJECTED, 0L).intValue();


            Map<String, UserResponse> map = new LinkedHashMap<>();

            for (UserProjection row : rows) {

                map.putIfAbsent(row.getUserId(), buildDealer(row));

                UserResponse dealer = map.get(row.getUserId());

                if (row.getAttributeKey() != null) {
                    dealer.addAttribute(row);
                }

                if (row.getDocumentId() != null) {
                    dealer.addDocument(row);
                }
            }

            return Map.of(
                    "code", 200,
                    "message", "Dealer list fetched successfully",
                    "data", Map.of(
                            "pendingcount", pending,
                            "approvedcount", approved,
                            "rejectedcount", rejected,
                            "content", map.values()
                    )
            );
        }

        // =====================================================
        // ✅ MOBILE FLOW
        // =====================================================

        UserBasicProjection user = dealerRepository.getUserBasic(userId);

        if (user == null) {
            throw new RuntimeException("User not found");
        }

        String role = user.getUserType();
        String island = user.getIsland();

        List<Map<String, Object>> data;

        if ("RECEIVER".equalsIgnoreCase(role)) {

            data = dealerRepository.fetchReceiverDealers(island);

        } else if ("PARTNER_AGENT".equalsIgnoreCase(role)) {

            data = dealerRepository.fetchPartnerAgentDealers(island);

        } else if ("SUPER_DEALER".equalsIgnoreCase(role)) {

            String subCategory = dealerRepository.getSubCategory(userId);

            if (subCategory == null) {
                throw new RuntimeException("Super dealer sub-category not found");
            }

            data = dealerRepository.fetchSuperDealerDealers(userId, subCategory);

        } else {
            data = List.of();
        }

        return Map.of(
                "code", 200,
                "message", "Dealer list fetched successfully",
                "data", Map.of(
                        "count", data.size(),
                        "content", data
                )
        );
    }

    private String mapStatus(String status) {
        if (status == null) return null;

        return switch (status.toLowerCase()) {
            case "pending" -> "PENDING";
            case "approved" -> "APPROVED";
            case "rejected" -> "REJECTED";
            default -> null;
        };
    }

    private UserResponse buildDealer(UserProjection row) {
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
}