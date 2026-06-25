package com.bank.account.service;

import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bank.account.dto.AccountDetailDto;
import com.bank.account.dto.CreateAccountRequest;
import com.bank.account.dto.CreateAccountResponse;
import com.bank.account.dto.GetAccountResponse;
import com.bank.account.exception.AccountAlreadyExistsException;
import com.bank.account.exception.CustomerNotFoundException;
import com.bank.account.model.AccountType;
import com.bank.account.model.CustomerAccount;
import com.bank.account.repository.CustomerAccountRepository;

@Service
public class AccountService {

    private static final Logger log = LoggerFactory.getLogger(AccountService.class);

    private final CustomerAccountRepository repository;

    public AccountService(CustomerAccountRepository repository) {
        this.repository = repository;
    }

    // ── POST /api/v1/account ─────────────────────────────────────────────────

    @Transactional
    public CreateAccountResponse createAccount(CreateAccountRequest request) {
        log.info("Creating account for email: {}", request.getCustomerEmail());

        repository.findByCustomerEmail(request.getCustomerEmail()).ifPresent(existing -> {
            throw new AccountAlreadyExistsException(
                    "An account with email '" + request.getCustomerEmail() + "' already exists.");
        });

        CustomerAccount account = new CustomerAccount();
        account.setCustomerName(request.getCustomerName());
        account.setCustomerMobile(request.getCustomerMobile());
        account.setCustomerEmail(request.getCustomerEmail());
        account.setAddress1(request.getAddress1());
        account.setAddress2(request.getAddress2());
        account.setAccountType(request.getAccountType());

        CustomerAccount saved = repository.save(account);
        log.info("Account created successfully with customerNumber: {}", saved.getId());

        return CreateAccountResponse.builder()
                .customerNumber(saved.getId())
                .transactionStatusCode(HttpStatus.CREATED.value())
                .transactionStatusDescription("Customer account created")
                .build();
    }

    // ── GET /api/v1/account/{customerNumber} ─────────────────────────────────

    @Transactional(readOnly = true)
    public GetAccountResponse getAccount(Long customerNumber) {
        log.info("Fetching account for customerNumber: {}", customerNumber);

        CustomerAccount account = repository.findById(customerNumber)
                .orElseThrow(() -> new CustomerNotFoundException(
                        "Customer not found"));

        AccountDetailDto accountDetail = new AccountDetailDto(
                account.getAccountNumber(),
                account.getAccountType() == AccountType.SAVINGS ? "Savings" : "Checking",
                account.getAvailableBalance()
        );

        GetAccountResponse.Builder builder = GetAccountResponse.builder()
                .customerNumber(account.getId())
                .customerName(account.getCustomerName())
                .customerMobile(account.getCustomerMobile())
                .customerEmail(account.getCustomerEmail())
                .address1(account.getAddress1())
                .address2(account.getAddress2())
                .transactionStatusCode(HttpStatus.FOUND.value())
                .transactionStatusDescription("Customer Account found");

        
        if (account.getAccountType() == AccountType.SAVINGS) {
            builder.savings(Collections.singletonList(accountDetail));
        } else {
            builder.checking(Collections.singletonList(accountDetail));
        }

        return builder.build();
    }
}
