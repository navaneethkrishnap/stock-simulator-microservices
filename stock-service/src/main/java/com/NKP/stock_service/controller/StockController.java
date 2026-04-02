package com.NKP.stock_service.controller;

import com.NKP.stock_service.dto.StockDTO;
import com.NKP.stock_service.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/stocks")
@RequiredArgsConstructor
public class StockController {

    private final StockService stockService;

    @GetMapping
    public ResponseEntity<List<StockDTO>> getAllStocks(){
        List<StockDTO> stocks = stockService.getAllStocks();
        return new ResponseEntity<>(stocks, HttpStatus.OK);
    }

    @GetMapping("/{symbol}")
    public ResponseEntity<StockDTO> getStock(@PathVariable String symbol){
        StockDTO stock = stockService.getStock(symbol);
        return new ResponseEntity<>(stock, HttpStatus.OK);
    }
}
