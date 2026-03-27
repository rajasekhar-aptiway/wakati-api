package com.wakati.repository;

import com.wakati.entity.DealerAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DealerAssignmentRepository extends JpaRepository<DealerAssignment, Long> {

    Optional<DealerAssignment> findByAssigmentId(String assigmentId);

    Optional<DealerAssignment> findByReceiverId(String receiverId);

    Optional<DealerAssignment> findByDealerId(String DealerId);

    Optional<DealerAssignment> findBySuperDealerId(String superDealerId);

    List<DealerAssignment> findByStatus(String status);
}
