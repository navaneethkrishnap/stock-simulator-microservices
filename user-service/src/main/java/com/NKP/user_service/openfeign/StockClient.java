package com.NKP.user_service.openfeign;

import com.NKP.user_service.dto.stock.StockDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient("STOCK-SERVICE")
public interface StockClient {

    @GetMapping("/stocks")
    List<StockDTO> getAllStocks();

    @GetMapping("/stocks/{symbol}")
    StockDTO getStock(@PathVariable String symbol);
}
