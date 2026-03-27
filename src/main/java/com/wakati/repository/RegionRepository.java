package com.wakati.repository;

import com.wakati.entity.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RegionRepository extends JpaRepository<Region, Long> {

    public Optional<Region> findByCode(String code);

    public List<Region> findByIsland(String island);

    public List<Region> findAllBy();
}
