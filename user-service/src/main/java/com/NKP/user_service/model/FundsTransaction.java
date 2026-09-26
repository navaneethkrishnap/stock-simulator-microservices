package com.NKP.user_service.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter @Setter @Builder @AllArgsConstructor @NoArgsConstructor
@Table(name = "funds_transaction")
public class FundsTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, unique = true)
    private Long orderId;

    @Column(nullable = false)
    private long userId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private FundsTransactionType type;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private boolean reversed = false;

    @Column(nullable = false)
    private LocalDateTime createdAt;
}
