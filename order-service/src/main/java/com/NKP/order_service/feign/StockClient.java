package com.NKP.order_service.feign;

import com.NKP.order_service.dto.stock.StockDTO;
import jakarta.ws.rs.Path;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient("STOCK-SERVICE")
public interface StockClient {

    @GetMapping("/stocks/{symbol}")
    StockDTO getStock(@PathVariable String symbol);
}
