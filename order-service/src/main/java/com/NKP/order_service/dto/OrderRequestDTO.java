package com.NKP.order_service.dto;

import com.NKP.order_service.model.OrderType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderRequestDTO {
    @NotNull
    private Long userId;
    @NotBlank
    private String symbol;
    @Min(value = 1, message = "Select at least 1 quantity")
    private Long quantity;
    @NotBlank
    private OrderType type;
}

