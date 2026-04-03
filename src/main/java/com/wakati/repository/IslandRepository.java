package com.wakati.repository;

import com.wakati.entity.Island;
import com.wakati.enums.IslandStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface IslandRepository extends JpaRepository<Island,Long> {

    public Optional<Island> findByCode(String code);

    public List<Island> findByStatus(IslandStatus status);

    public List<Island> findAllBy();

    public Optional<Island> findByCodeAndStatus(String code, IslandStatus status);

    @Query(value = """
        SELECT i.id AS island_id, i.code AS island_code,
               i.name_en, i.name_fr, i.name_km,
               r.id AS region_id, r.code AS region_code,
               r.region_en, r.region_fr, r.region_km
        FROM ISLANDS i
        LEFT JOIN REGIONS r ON i.code = r.island
        ORDER BY i.name_en, r.region_en
    """, nativeQuery = true)
    List<Map<String, Object>> fetchAll();


    @Query(value = "SELECT COUNT(*) > 0 FROM ISLANDS WHERE code = :code", nativeQuery = true)
    boolean existsIsland(String code);

    @Query(value = "SELECT COUNT(*) > 0 FROM ISLANDS WHERE name_en=?1 OR name_fr=?2 OR name_km=?3", nativeQuery = true)
    boolean existsIslandName(String en, String fr, String km);

    @Modifying
    @Query(value = """
        INSERT INTO ISLANDS(code,name_en,name_fr,name_km,status,created_at)
        VALUES (?1,?2,?3,?4,?5,NOW())
    """, nativeQuery = true)
    void insertIsland(String code, String en, String fr, String km, String status);


    @Query(value = "SELECT COUNT(*) > 0 FROM ISLANDS WHERE code=?1 AND status='ACTIVE'", nativeQuery = true)
    boolean existsActiveIsland(String code);

    @Query(value = "SELECT COUNT(*) > 0 FROM REGIONS WHERE code=?1 AND island=?2", nativeQuery = true)
    boolean existsRegion(String code, String island);

    @Query(value = "SELECT COUNT(*) > 0 FROM REGIONS WHERE island=?1 AND (region_en=?2 OR region_fr=?3 OR region_km=?4)", nativeQuery = true)
    boolean existsRegionName(String island, String en, String fr, String km);

    @Modifying
    @Query(value = """
        INSERT INTO REGIONS(code,island,region_en,region_fr,region_km,created_at)
        VALUES (?1,?2,?3,?4,?5,NOW())
    """, nativeQuery = true)
    void insertRegion(String code, String island, String en, String fr, String km);


    @Modifying
    @Query(value = "UPDATE ISLANDS SET name_en=?2,name_fr=?3,name_km=?4,status=?5 WHERE code=?1", nativeQuery = true)
    void updateIsland(String code, String en, String fr, String km, String status);

    @Modifying
    @Query(value = "UPDATE REGIONS SET region_en=?3,region_fr=?4,region_km=?5 WHERE code=?1 AND island=?2", nativeQuery = true)
    void updateRegion(String code, String island, String en, String fr, String km);


    @Query(value = "SELECT COUNT(*) FROM REGIONS WHERE island=?1", nativeQuery = true)
    int countRegions(String island);

    @Modifying
    @Query(value = "DELETE FROM ISLANDS WHERE code=?1", nativeQuery = true)
    void deleteIsland(String code);

    @Modifying
    @Query(value = "DELETE FROM Regions WHERE code=?1", nativeQuery = true)
    void deleteRegion(String code);
}
