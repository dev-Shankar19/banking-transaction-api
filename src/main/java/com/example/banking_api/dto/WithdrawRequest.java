package com.example.banking_api.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class WithdrawRequest {

    @NotNull(message = "Account id is required")
    private Long accountId;


    @NotNull(message = "Amount is required")
    @Positive(message = "Withdraw amount must be positive")
    private BigDecimal amount;
}
