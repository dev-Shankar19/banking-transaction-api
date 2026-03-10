package com.example.banking_api.controller;

import org.springframework.security.core.Authentication;
import com.example.banking_api.dto.*;
import com.example.banking_api.entity.Account;
import com.example.banking_api.entity.Transactions;
import com.example.banking_api.service.AccountService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService){
        this.accountService = accountService;
    }

    @PostMapping
    public ResponseEntity<Account> createAccount(@Valid @RequestBody CreateAccountRequest request){
        return ResponseEntity.ok(accountService.createAccount(request));
    }

    @PostMapping("/deposit")
    public ResponseEntity<Account> deposit(@Valid @RequestBody DepositRequest request){
        return ResponseEntity.ok(accountService.deposit(request));
    }

    @PostMapping("/withdraw")
    public ResponseEntity<Account> withdraw(@Valid @RequestBody WithdrawRequest request){
        return ResponseEntity.ok(accountService.withdraw(request));
    }

    @PostMapping("/transfer")
    public ResponseEntity<TransferResponse> transfer(
            @RequestHeader("Idempotency-Key") String key,
            @Valid @RequestBody TransferRequest request){
        return ResponseEntity.ok(accountService.transfer(request,key));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Account> getAccount(
            @PathVariable Long id,
            Authentication authentication){

        String username = authentication.getName();

        return ResponseEntity.ok(
                accountService.getAccount(id, username)
        );
    }

    @GetMapping
    public Page<AccountResponse> getAccounts(
            @RequestParam @Min(0) int page,
            @RequestParam @Min(1) @Max(100) int size
    ){
        return accountService.getAccounts(page,size);
    }


    @GetMapping("/transactions")
    public Page<Transactions> getTransactions(
            @RequestParam Long accountId,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size,
            Authentication authentication
    ){

        String username = authentication.getName();

        return accountService.getTransactionHistory(accountId, username, page, size);
    }
}
