package com.example.banking_api.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransferRequest{

    @NotNull(message = "From account is required")
    private Long fromAccount;

    @NotNull(message = "To account is required")
    private Long toAccount;

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be greater than zero")
    private BigDecimal amount;

}
