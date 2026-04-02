package com.NKP.stock_service.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Stock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(unique = true, nullable = false)
    private String symbol;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private BigDecimal currentMarketPrice;
    @Column(nullable = false)
    private BigDecimal high52w;
    @Column(nullable = false)
    private BigDecimal low52w;
}
