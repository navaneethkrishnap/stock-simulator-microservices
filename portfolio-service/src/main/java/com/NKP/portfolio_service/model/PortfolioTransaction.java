package com.NKP.portfolio_service.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter @Setter @Builder @AllArgsConstructor @NoArgsConstructor
@Table(name = "portfolio_transactions")
public class PortfolioTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,unique = true)
    private Long orderId;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String symbol;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType type;

    @Column(nullable = false)
    private long quantityDelta;

    @Column(nullable = false)
    private BigDecimal investmentDelta;

    @Column(nullable = false)
    @Builder.Default
    private boolean reversed = false;

    @Column(nullable = false)
    private LocalDateTime createdAt;
}
