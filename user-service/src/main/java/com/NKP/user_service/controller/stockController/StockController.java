package com.NKP.user_service.controller.stockController;

import com.NKP.user_service.dto.stock.StockDTO;
import com.NKP.user_service.service.stockService.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/stocks")
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
