package com.example.banking_api.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
@JsonPropertyOrder({"id","accountNumber","balance"})
public class AccountResponse {

    private final Long id;
    private final String accountNumber;
    private final BigDecimal balance;
}
