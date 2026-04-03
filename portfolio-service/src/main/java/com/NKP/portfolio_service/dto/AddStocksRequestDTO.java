package com.NKP.portfolio_service.dto;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AddStocksRequestDTO {

    @NotNull
    private Long userId;
    @NotBlank
    private String stockName;
    @NotBlank
    private String symbol;
    @Min(value = 1,message = "Quantity must be equal to or more than 1")
    private long quantities;
    @NotBlank
    private BigDecimal orderPrice;
}
