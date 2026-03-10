package com.example.banking_api.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(indexes = {
        @Index(name = "idx_from_account", columnList = "fromAccount"),
        @Index(name = "idx_to_account",columnList = "toAccount")
})
@NoArgsConstructor
@AllArgsConstructor
public class Transactions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long fromAccount;
    private Long toAccount;
    private BigDecimal amount;
    private String type;
    private String status;
    private LocalDateTime createdAt;


}
