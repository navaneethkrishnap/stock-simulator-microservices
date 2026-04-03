package com.NKP.portfolio_service.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class DeductStocksRequestDTO {
    @NotNull
    private Long userId;
    @NotBlank
    private String stockName;
    @NotBlank
    private String symbol;
    @Min(value = 1, message = "Select at least 1 quantity to sell")
    private long quantities;
}
