package com.NKP.portfolio_service.repo;

import com.NKP.portfolio_service.model.Portfolio;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PortfolioRepository extends JpaRepository<Portfolio,Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Portfolio> findByUserIdAndSymbol(long userId, String stockSymbol);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Portfolio> findByUserIdAndSymbolAndStockName(long userId, String symbol, String stockName);
}
