package com.example.banking_api.service;


import com.example.banking_api.dto.*;
import com.example.banking_api.entity.*;
import com.example.banking_api.exception.InsufficientFundsException;
import com.example.banking_api.exception.ResourceNotFoundException;
import com.example.banking_api.repository.AccountRepository;
import com.example.banking_api.repository.IdempotencyRepository;
import com.example.banking_api.repository.TransactionRepository;
import com.example.banking_api.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AccountService{
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final IdempotencyRepository idempotencyRepository;

    public Page<Transactions> getTransactionHistory(Long accountId,String username, int page, int size){
        Pageable pageable =
                PageRequest.of(page, size,
                        Sort.by("createdAt").descending());


        return transactionRepository
                .findAccountTransactions(accountId, pageable);
    }
    public AccountService (AccountRepository accountRepository,
                           UserRepository userRepository,
                           TransactionRepository transactionRepository, IdempotencyRepository idempotencyRepository){
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
        this.idempotencyRepository = idempotencyRepository;
    }


    @Transactional
    public Account deposit(DepositRequest request){
        Account account = accountRepository.findById(request.getAccountId())
                .orElseThrow(()-> new RuntimeException("Account not found"));
        BigDecimal newBalance = account.getBalance().add(request.getAmount());

        account.setBalance(newBalance);
        accountRepository.save(account);

        Transactions tx = new Transactions();
        tx.setFromAccount(null);
        tx.setToAccount(account.getId());
        tx.setAmount(request.getAmount());
        tx.setType("DEPOSIT");
        tx.setStatus("SUCCESS");
        tx.setCreatedAt(LocalDateTime.now());
        transactionRepository.save(tx);
        return account;

    }


    @Transactional
    public Account withdraw(WithdrawRequest request){
        Account account = accountRepository.findById(request.getAccountId())
                .orElseThrow(()-> new RuntimeException("Account not found"));
        if (account.getBalance().compareTo(request.getAmount())<0){
            throw new InsufficientFundsException("Insufficient funds");

        }
        BigDecimal newBalance = account.getBalance().subtract(request.getAmount());
        account.setBalance(newBalance);
        accountRepository.save(account);
        Transactions tx = new Transactions();

        tx.setFromAccount(account.getId());
        tx.setToAccount(null);
        tx.setType("WITHDRAW");
        tx.setStatus("SUCCESS");
        tx.setAmount(request.getAmount());
        tx.setCreatedAt(LocalDateTime.now());
        transactionRepository.save(tx);
        return account;
    }


    @Transactional
    public TransferResponse transfer(TransferRequest request, String key){

        if(idempotencyRepository.existsById(key)){
            throw new RuntimeException("Duplicate transfer request");
        }

        if(request.getFromAccount().equals(request.getToAccount())){
            throw new IllegalArgumentException("Cannot transfer to same account");
        }

        Account sender = accountRepository.findById(request.getFromAccount())
                .orElseThrow(() -> new ResourceNotFoundException("From account not found"));

        Account receiver = accountRepository.findById(request.getToAccount())
                .orElseThrow(() -> new ResourceNotFoundException("To account not found"));

        if(sender.getBalance().compareTo(request.getAmount()) < 0){
            throw new InsufficientFundsException("Insufficient funds");
        }

        sender.setBalance(sender.getBalance().subtract(request.getAmount()));
        receiver.setBalance(receiver.getBalance().add(request.getAmount()));

        Transactions tx = new Transactions();
        tx.setFromAccount(sender.getId());
        tx.setToAccount(receiver.getId());
        tx.setAmount(request.getAmount());
        tx.setType("TRANSFER");
        tx.setStatus("SUCCESS");
        tx.setCreatedAt(LocalDateTime.now());

        transactionRepository.save(tx);

        IdempotencyKey record = new IdempotencyKey();
        record.setKey(key);
        record.setRequestHash("TRANSFER");
        record.setRespondBody("SUCCESS");
        record.setCreatedAt(LocalDateTime.now());

        idempotencyRepository.save(record);

        return new TransferResponse(
                sender.getId(),
                receiver.getId(),
                request.getAmount(),
                "SUCCESS"
        );
    }


    public Account createAccount(CreateAccountRequest request) {

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (request.getInitialBalance().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Initial balance cannot be negative");
        }

        Account account = new Account();
        account.setAccountNumber(generateAccountNumber());
        account.setBalance(request.getInitialBalance());
        account.setUser(user);
        account.setCreatedAt(LocalDateTime.now());

        return accountRepository.save(account);
    }

    private String generateAccountNumber(){
        return UUID.randomUUID().toString().replace("-","").substring(0,12);

    }

    public Page<AccountResponse> getAccounts(int page, int size){

        Pageable pageable =
                PageRequest.of(page, size, Sort.by("createdAt").descending());

        Page<Account> accounts = accountRepository.findAll(pageable);

        return accounts.map(a ->
                new AccountResponse(
                        a.getId(),
                        a.getAccountNumber(),
                        a.getBalance()
                )
        );
    }

    public Account getAccount(Long id, String username) {

        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        if (!account.getUser().getUsername().equals(username)) {
            throw new RuntimeException("Unauthorized access");
        }

        return account;
    }
}
