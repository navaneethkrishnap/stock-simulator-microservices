package com.NKP.user_service.dto.funds;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class WithdrawMoneyRequestDTO {

    private long userId;
    private BigDecimal amount;
}
