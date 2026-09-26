package com.NKP.user_service.repo;

import com.NKP.user_service.model.FundsTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FundsTransactionRepository extends JpaRepository<FundsTransaction, Long> {

    Optional<FundsTransaction> findByOrderId(Long orderId);

}
