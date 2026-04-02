package com.NKP.bank_service.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class WithdrawMoneyRequestDTO {
    private long userId;
    private BigDecimal amount;
}
