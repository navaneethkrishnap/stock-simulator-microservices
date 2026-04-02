package com.NKP.portfolio_service.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Portfolio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private long userId;

    @Column(nullable = false)
    private String stockName;

    @Column(nullable = false)
    private String symbol;

    @Column(nullable = false)
    private long quantities;

    @Column(nullable = false)
    private BigDecimal avgHoldingsPrice;

    @Column(nullable = false)
    private BigDecimal totalInvestment;
}
