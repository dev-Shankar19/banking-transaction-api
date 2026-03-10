package com.example.banking_api.dto;


import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CreateAccountRequest {
    private Long userId;

    private BigDecimal initialBalance;
}
