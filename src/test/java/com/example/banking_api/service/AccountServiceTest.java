package com.example.banking_api.service;

import com.example.banking_api.dto.TransferRequest;
import com.example.banking_api.dto.TransferResponse;
import com.example.banking_api.entity.User;
import com.example.banking_api.exception.InsufficientFundsException;
import com.example.banking_api.entity.Account;
import com.example.banking_api.*;
import com.example.banking_api.repository.AccountRepository;
import com.example.banking_api.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AccountServiceTest {

    @Autowired
    private AccountService accountService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AccountRepository accountRepository;
    @BeforeEach

    void setup(){

        User user = new User();
        user.setUsername("john");
        user.setEmail("john@email.com");
        user.setPassword("pass");
        userRepository.save(user);

        Account a1 = new Account();
        a1.setAccountNumber("111");
        a1.setBalance(new BigDecimal("500"));
        a1.setUser(user);
        accountRepository.save(a1);

        Account a2 = new Account();
        a2.setAccountNumber("222");
        a2.setBalance(new BigDecimal("100"));
        a2.setUser(user);
        accountRepository.save(a2);
    }

    @Test
    void transferShouldWork(){

        TransferRequest req = new TransferRequest();
        req.setFromAccount(1L);
        req.setToAccount(2L);
        req.setAmount(new BigDecimal("100"));

        TransferResponse result =
                accountService.transfer(req,"test-key-1");

        assertEquals(1L,result.getFromAccount());
        assertEquals(2L,result.getToAccount());
        assertEquals(new BigDecimal("100"),result.getAmount());
        assertEquals("SUCCESS",result.getStatus());
    }

    @Test
    void transferShouldFailWhenSameAccount(){

        TransferRequest req = new TransferRequest();
        req.setFromAccount(1L);
        req.setToAccount(1L);
        req.setAmount(new BigDecimal("100"));

        assertThrows(
                RuntimeException.class,
                () -> accountService.transfer(req,"test-key-3")
        );
    }
    @Test
    void transferShouldFailWhenBalanceLow() {

        TransferRequest req = new TransferRequest();
        req.setFromAccount(1L);
        req.setToAccount(2L);
        req.setAmount(new BigDecimal("1000000"));

        assertThrows(
                InsufficientFundsException.class,
                () -> accountService.transfer(req, "test-key-2")
        );
    }

}