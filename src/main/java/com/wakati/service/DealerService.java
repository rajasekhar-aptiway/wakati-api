package com.wakati.service;

import com.wakati.I18NConstants;
import com.wakati.enums.Status;
import com.wakati.exception.WakatiException;
import com.wakati.model.request.DealerRequest;
import com.wakati.model.response.ResponseBuilder;
import com.wakati.model.response.UserBasicProjection;
import com.wakati.model.response.UserProjection;
import com.wakati.model.response.UserResponse;
import com.wakati.repository.DealerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DealerService {

    @Autowired
    private DealerRepository dealerRepository;

    @Autowired
    private ResponseBuilder responseBuilder;


    public Map<String, Object> getDealers(DealerRequest request) {

        String platform = request.getPlatform();
        String statusParam = String.valueOf(request.getStatus());
        String userId = request.getUserId();

        // ✅ VALIDATION
        if (!"web".equalsIgnoreCase(platform) && userId == null) {
            throw new WakatiException(I18NConstants.USER_ID_REQUIRED);
        }

        String status = mapStatus(statusParam);
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

            return responseBuilder.success(I18NConstants.DEALER_LIST_FETCHED_SUCCESSFULLY,"data",Map.of(
                    "pendingcount", pending,
                    "approvedcount", approved,
                    "rejectedcount", rejected,
                    "content", map.values()
            ));
        }

       //Mobile flow

        UserBasicProjection user = dealerRepository.getUserBasic(userId);

        if (user == null) {
            throw new WakatiException(HttpStatus.NO_CONTENT,I18NConstants.USER_NOT_FOUND);
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
                throw new WakatiException(HttpStatus.NO_CONTENT,I18NConstants.SUPER_DEALER_SUB_CATEGORY_NOT_FOUND);
            }
            data = dealerRepository.fetchSuperDealerDealers(userId, subCategory);

        } else {
            data = List.of();
        }
        return responseBuilder.success(I18NConstants.DEALER_LIST_FETCHED_SUCCESSFULLY,"data",Map.of(
                "count", data.size(),
                "content", data
        ));
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