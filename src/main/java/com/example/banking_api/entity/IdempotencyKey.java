package com.example.banking_api.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IdempotencyKey{
    @Id
    private String key;

    private String requestHash;


    @Column(columnDefinition = "TEXT")
    private String respondBody;

    private LocalDateTime createdAt;
}
