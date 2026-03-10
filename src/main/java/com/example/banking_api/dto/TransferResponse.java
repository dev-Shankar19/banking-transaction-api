package com.example.banking_api.dto;

import java.math.BigDecimal;

public class TransferResponse {

    private Long fromAccount;
    private Long toAccount;
    private BigDecimal amount;
    private String status;

    public TransferResponse(Long fromAccount, Long toAccount,
                            BigDecimal amount, String status) {
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.amount = amount;
        this.status = status;
    }

    public Long getFromAccount() { return fromAccount; }
    public Long getToAccount() { return toAccount; }
    public BigDecimal getAmount() { return amount; }
    public String getStatus() { return status; }
}