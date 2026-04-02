package com.NKP.order_service.feign;

import com.NKP.order_service.dto.portfolfio.AddStocksRequestDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "PORTFOLIO-SERVICE", url = "http://localhost:8085")
public interface PortfolioClient {

    @PostMapping("portfolio/add-stock")
    ResponseEntity<String> addStockIntoAccount(@RequestBody AddStocksRequestDTO requestDTO);
}
