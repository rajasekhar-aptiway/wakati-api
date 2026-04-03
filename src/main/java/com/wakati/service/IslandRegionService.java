package com.wakati.service;

import com.wakati.config.UserContextHolder;
import com.wakati.entity.User;
import com.wakati.model.request.IslandRegionRequest;
import com.wakati.repository.IslandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class IslandRegionService {

    @Autowired
    private IslandRepository repo;

    public Map<String, Object> fetch() {

        List<Map<String, Object>> rows = repo.fetchAll();

        Map<String, Map<String, Object>> islands = new LinkedHashMap<>();

        for (Map<String, Object> row : rows) {

            String islandCode = (String) row.get("island_code");

            islands.putIfAbsent(islandCode, new LinkedHashMap<>(Map.of(
                    "islandId", row.get("island_id"),
                    "islandCode", islandCode,
                    "name_en", row.get("name_en"),
                    "name_fr", row.get("name_fr"),
                    "name_km", row.get("name_km"),
                    "regions", new LinkedHashMap<String, Object>()
            )));

            if (row.get("region_id") != null) {

                Map<String, Object> island = islands.get(islandCode);
                Map<String, Object> regions =
                        (Map<String, Object>) island.get("regions");

                String regionId = String.valueOf(row.get("region_id"));

                regions.putIfAbsent(regionId, Map.of(
                        "regionId", row.get("region_id"),
                        "regionCode", row.get("region_code"),
                        "region_en", row.get("region_en"),
                        "region_fr", row.get("region_fr"),
                        "region_km", row.get("region_km")
                ));
            }
        }

        List<Map<String, Object>> result = islands.values().stream().map(i -> {
            Map<String, Object> map = new HashMap<>(i);
            map.put("regions",
                    new ArrayList<>(((Map<?, ?>) i.get("regions")).values()));
            return map;
        }).toList();

        return Map.of(
                "code", 200,
                "message", "Islands and regions fetched successfully",
                "data", Map.of(
                        "totalIslands", result.size(),
                        "content", result
                )
        );
    }

    public Map<String, Object> handle(IslandRegionRequest req) {

        User user = UserContextHolder.getUser();

        if (!List.of("ADMIN", "CFO").contains(user.getUserType().name())) {
            throw new RuntimeException("Access denied");
        }

        String action = Optional.ofNullable(req.getAction()).orElse("").toLowerCase();

        return switch (action) {

            case "add_island" -> addIsland(req);
            case "add_region" -> addRegion(req);
            case "update_island" -> updateIsland(req);
            case "update_region" -> updateRegion(req);
            case "delete_island" -> deleteIsland(req);
            case "delete_region" -> deleteRegion(req);

            default -> throw new RuntimeException("Invalid action");
        };
    }

    private Map<String, Object> addIsland(IslandRegionRequest r) {

        String code = r.getCode().toUpperCase();

        if (repo.existsIsland(code)) {
            throw new RuntimeException("Island already exists");
        }

        if (repo.existsIslandName(r.getNameEn(), r.getNameFr(), r.getNameKm())) {
            throw new RuntimeException("Duplicate island name");
        }

        repo.insertIsland(code, r.getNameEn(), r.getNameFr(), r.getNameKm(), r.getStatus());

        return Map.of("message", "Island added", "code", code);
    }

    private Map<String, Object> addRegion(IslandRegionRequest r) {

        if (!repo.existsActiveIsland(r.getIslandCode())) {
            throw new RuntimeException("Island not active");
        }

        if (repo.existsRegion(r.getCode(), r.getIslandCode())) {
            throw new RuntimeException("Region exists");
        }

        if (repo.existsRegionName(r.getIslandCode(), r.getRegionEn(), r.getRegionFr(), r.getRegionKm())) {
            throw new RuntimeException("Duplicate region name");
        }

        repo.insertRegion(
                r.getCode(),
                r.getIslandCode(),
                r.getRegionEn(),
                r.getRegionFr(),
                r.getRegionKm()
        );

        return Map.of("message", "Region added");
    }

    private Map<String, Object> updateIsland(IslandRegionRequest r) {
        repo.updateIsland(r.getCode(), r.getNameEn(), r.getNameFr(), r.getNameKm(), r.getStatus());
        return Map.of("message", "Island updated");
    }

    private Map<String, Object> updateRegion(IslandRegionRequest r) {
        repo.updateRegion(r.getCode(), r.getIslandCode(), r.getRegionEn(), r.getRegionFr(), r.getRegionKm());
        return Map.of("message", "Region updated");
    }

    private Map<String, Object> deleteIsland(IslandRegionRequest r) {

        if (repo.countRegions(r.getCode()) > 0) {
            throw new RuntimeException("Cannot delete island with regions");
        }

        repo.deleteIsland(r.getCode());
        return Map.of("message", "Island deleted");
    }

    private Map<String, Object> deleteRegion(IslandRegionRequest r) {
        repo.deleteRegion(r.getCode());
        return Map.of("message", "Region deleted");
    }
}