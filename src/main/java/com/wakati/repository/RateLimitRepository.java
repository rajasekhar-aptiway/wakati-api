package com.wakati.repository;

import com.wakati.entity.RateLimit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RateLimitRepository extends JpaRepository<RateLimit, Long> {

    public List<RateLimit> findByEndpoint(String endpoint);

    public List<RateLimit> findByKeyTypeAndKeyValue(String keyType, String keyValue);

    public List<RateLimit> findByEndpointAndKeyTypeAndKeyValue(
            String endpoint, String keyType, String keyValue);
}
