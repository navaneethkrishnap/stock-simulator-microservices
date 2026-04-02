package com.NKP.order_service.dto;

import com.NKP.order_service.model.OrderStatus;
import com.NKP.order_service.model.OrderType;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderResponseDTO {
    private long userId;
    private String symbol;
    private long quantity;
    private OrderType type;
    private BigDecimal orderPrice;
    private OrderStatus status;
}
