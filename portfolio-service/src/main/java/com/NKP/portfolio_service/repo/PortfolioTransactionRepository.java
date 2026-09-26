package com.NKP.portfolio_service.repo;

import com.NKP.portfolio_service.model.PortfolioTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PortfolioTransactionRepository extends JpaRepository<PortfolioTransaction,Long> {
    Optional<PortfolioTransaction> findByOrderId(Long orderId);
}
