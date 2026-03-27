package com.wakati.repository;

import com.wakati.entity.SecurityDeposit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SecurityDepositRepository extends JpaRepository<SecurityDeposit, Long> {

    public Optional<SecurityDeposit> findByCode(String code);

    public List<SecurityDeposit> findByTypeOfDealer(String typeOfDealer);

    public List<SecurityDeposit> findByStatus(String status);

    public List<SecurityDeposit> findByTypeOfDealerAndStatus(String typeOfDealer, String status);
}
