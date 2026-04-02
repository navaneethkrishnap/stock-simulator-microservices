package com.NKP.user_service.dto.order;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderPaymentRequestDTO {
    private long userId;
    private BigDecimal amount;
}
