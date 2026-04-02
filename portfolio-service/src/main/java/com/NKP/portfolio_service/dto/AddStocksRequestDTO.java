package com.NKP.portfolio_service.dto;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AddStocksRequestDTO {

    private long userId;
    private String stockName;
    private String symbol;
    private long quantities;
    private BigDecimal orderPrice;
}
