package com.NKP.order_service.dto.portfolfio;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DeductStocksRequestDTO {
    @NotNull
    private Long userId;
    @NotBlank
    private String stockName;
    @NotBlank
    private String symbol;
    @Min(value = 1, message = "Select at least 1 quantity to sell")
    private Long quantities;
}
