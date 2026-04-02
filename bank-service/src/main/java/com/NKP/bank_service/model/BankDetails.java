package com.NKP.bank_service.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class BankDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false)
    private long userId;
    @Column(nullable = false)
    private String bankName;
    @Column(nullable = false,unique = true)
    private String accountNumber;
    @Column(nullable = false)
    private String accountName;
    @Column(nullable = false)
    private String branchName;
    @Column(nullable = false)
    private String ifsc; // hash and store
    @Column(nullable = false)
    private String aadhaarNumber; // hash and store
    @Column(nullable = false)
    private String panNumber; // hash and store
//    private BigDecimal balanceTrade;

}
