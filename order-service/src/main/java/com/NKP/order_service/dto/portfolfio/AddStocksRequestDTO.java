package com.NKP.order_service.dto.portfolfio;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class AddStocksRequestDTO {
    private long userId;
    private String stockName;
    private String symbol;
    private long quantities;
    private BigDecimal orderPrice;
}
