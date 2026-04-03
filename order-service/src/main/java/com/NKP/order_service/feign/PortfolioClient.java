package com.NKP.order_service.feign;

import com.NKP.order_service.dto.portfolfio.AddStocksRequestDTO;
import com.NKP.order_service.dto.portfolfio.DeductStocksRequestDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;

@FeignClient(name = "PORTFOLIO-SERVICE", url = "http://localhost:8085")
public interface PortfolioClient {

    @PostMapping("portfolio/add-stock")
    ResponseEntity<String> addStockIntoAccount(@RequestBody AddStocksRequestDTO requestDTO);

    @PostMapping("portfolio/deduct-stock")
    ResponseEntity<String> deductStockFromAccount(@RequestBody DeductStocksRequestDTO requestDTO);

    @PostMapping("porfolio/redo-deduct-stock")
    ResponseEntity<String> redoStockDeductedFromAccount(DeductStocksRequestDTO deductStocksRequestDTO);
}