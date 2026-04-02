package com.NKP.stock_service.service;

import com.NKP.stock_service.dto.StockDTO;
import com.NKP.stock_service.model.Stock;
import com.NKP.stock_service.repo.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StockService {

    private final StockRepository stockRepository;

    public List<StockDTO> getAllStocks() {

        List<Stock> stocks = stockRepository.findAll();

        List<StockDTO> stockDTOS = new ArrayList<>();

        for(Stock stock : stocks){

            StockDTO stockDTO = new StockDTO();

            stockDTO.setCMP(stock.getCurrentMarketPrice());
            stockDTO.setName(stock.getName());
            stockDTO.setSymbol(stock.getSymbol());
            stockDTO.setLow52w(stock.getLow52w());
            stockDTO.setHigh52w(stock.getHigh52w());

            stockDTOS.add(stockDTO);
        }

        return stockDTOS;
    }

    public StockDTO getStock(String symbol) {

        Stock stock = stockRepository.findBySymbol(symbol);
        StockDTO stockDTO = new StockDTO();
        stockDTO.setName(stock.getName());
        stockDTO.setCMP(stock.getCurrentMarketPrice());
        stockDTO.setSymbol(stock.getSymbol());
        stockDTO.setLow52w(stock.getLow52w());
        stockDTO.setHigh52w(stock.getHigh52w());

        return stockDTO;
    }
}
