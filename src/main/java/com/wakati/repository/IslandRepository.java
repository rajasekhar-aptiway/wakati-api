package com.wakati.repository;

import com.wakati.entity.Island;
import com.wakati.enums.IslandStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IslandRepository extends JpaRepository<Island,Long> {

    public Optional<Island> findByCode(String code);

    public List<Island> findByStatus(IslandStatus status);

    public List<Island> findAllBy();

    public Optional<Island> findByCodeAndStatus(String code, IslandStatus status);
}
