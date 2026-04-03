package com.NKP.portfolio_service.controller;

import com.NKP.portfolio_service.dto.AddStocksRequestDTO;
import com.NKP.portfolio_service.dto.DeductStocksRequestDTO;
import com.NKP.portfolio_service.service.PortfolioUpdateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/portfolio")
@RequiredArgsConstructor
public class UpdatePortfolio {

    private final PortfolioUpdateService portfolioUpdateService;

    @PostMapping("/add-stock")
    public ResponseEntity<String> addStockIntoAccount(@RequestBody AddStocksRequestDTO requestDTO){
        portfolioUpdateService.addStockIntoAccount(requestDTO);
        return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
    }

    @PostMapping("/deduct-stock")
    public ResponseEntity<String> deductStockFromAccount(@RequestBody DeductStocksRequestDTO requestDTO){
        portfolioUpdateService.deductStockFromAccount(requestDTO);
        return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
    }

    @PostMapping("/redo-deduct-stock")
    ResponseEntity<String> redoStockDeductedFromAccount(@RequestBody DeductStocksRequestDTO deductStocksRequestDTO){
        portfolioUpdateService.redoStockDeductedFromAccount(deductStocksRequestDTO);
        return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
    }
}
