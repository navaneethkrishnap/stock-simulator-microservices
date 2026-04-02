package com.NKP.portfolio_service.repo;

import com.NKP.portfolio_service.model.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PortfolioRepository extends JpaRepository<Portfolio,Long> {

    Optional<Portfolio> findByUserIdAndSymbol(long userId, String stockSymbol);
}
