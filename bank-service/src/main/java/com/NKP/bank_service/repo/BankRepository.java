package com.NKP.bank_service.repo;

import com.NKP.bank_service.model.BankDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BankRepository extends JpaRepository<BankDetails, Long> {

    Optional<BankDetails> findByUserId(long userId);
}
