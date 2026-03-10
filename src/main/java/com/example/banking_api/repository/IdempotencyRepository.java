package com.example.banking_api.repository;

import com.example.banking_api.entity.IdempotencyKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IdempotencyRepository extends JpaRepository<IdempotencyKey, String>{
    
}