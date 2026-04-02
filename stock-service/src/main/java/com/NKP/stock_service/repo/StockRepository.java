package com.NKP.stock_service.repo;

import com.NKP.stock_service.model.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockRepository extends JpaRepository<Stock,Long> {
    Stock findBySymbol(String symbol);
}
