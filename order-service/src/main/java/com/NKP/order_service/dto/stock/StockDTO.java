package com.NKP.order_service.dto.stock;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class StockDTO {
    private String symbol;
    private String name;
    private BigDecimal CMP;
    private BigDecimal high52w;
    private BigDecimal low52w;
}
