package com.example.banking_api.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter

public class Idempotency{
    @Id
    private String Key;
    private String requestHash;
    private String responseBody;

}

