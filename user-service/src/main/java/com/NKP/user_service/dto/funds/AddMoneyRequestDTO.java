package com.NKP.user_service.dto.funds;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AddMoneyRequestDTO {

    private long userId; // remove it when added security
    private BigDecimal amount;
}
