package com.NKP.user_service.service.stockService;

import com.NKP.user_service.dto.stock.StockDTO;
import com.NKP.user_service.openfeign.StockClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StockService {

    private final StockClient stockClient;

    public List<StockDTO> getAllStocks(){
        return stockClient.getAllStocks();
    }

    public StockDTO getStock(String symbol){
        return stockClient.getStock(symbol);
    }
}
