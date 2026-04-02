package com.NKP.user_service.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long userId;
    @Column(nullable = false, length = 80)
    private String fullName;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String emailId;
    @Column(nullable = false)
    private String phoneNo;
    @Column(nullable = false)
    private LocalDate dob;
    @Column(nullable = false)
    private BigDecimal availBalance;
}
