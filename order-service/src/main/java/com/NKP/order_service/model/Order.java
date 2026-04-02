package com.NKP.order_service.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.web.bind.annotation.GetMapping;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false)
    private long userId;
    @Column(nullable = false)
    private LocalDateTime orderPlacedTime;
    @Column(nullable = false)
    private OrderStatus status;
    @Column(nullable = false)
    private OrderType type;
    @Column(nullable = false)
    private BigDecimal orderPrice;
    @Column(nullable = false)
    private long quantity;
    @Column(nullable = false)
    private BigDecimal totalPrice;
}
