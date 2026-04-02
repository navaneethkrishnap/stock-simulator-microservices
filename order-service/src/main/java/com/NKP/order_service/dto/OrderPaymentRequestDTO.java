package com.NKP.order_service.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderPaymentRequestDTO {
    private long userId;
    private BigDecimal amount;
}
