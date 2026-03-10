package com.example.banking_api.repository;

import org.springframework.data.domain.Pageable;
import com.example.banking_api.entity.Transactions;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TransactionRepository extends JpaRepository<Transactions, Long> {

    @Query("""
        SELECT t
        FROM Transactions t
        WHERE t.fromAccount = :accountId
           OR t.toAccount = :accountId
    """)
    Page<Transactions> findAccountTransactions(
            Long accountId,
            Pageable pageable
    );

}