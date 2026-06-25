package com.bank.account.controller;

import com.bank.account.dto.CreateAccountRequest;
import com.bank.account.dto.CreateAccountResponse;
import com.bank.account.dto.GetAccountResponse;
import com.bank.account.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/account")
public class AccountController {

    private static final Logger log = LoggerFactory.getLogger(AccountController.class);

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    /**
     * POST /api/v1/account
     * Creates a new customer account.
     */
    @PostMapping
    public ResponseEntity<CreateAccountResponse> createAccount(
            @Valid @RequestBody CreateAccountRequest request) {
        log.info("Received account creation request for: {}", request.getCustomerEmail());
        CreateAccountResponse response = accountService.createAccount(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * GET /api/v1/account/{customerNumber}
     * Retrieves an existing customer account by customerNumber.
     */
    @GetMapping("/{customerNumber}")
    public ResponseEntity<GetAccountResponse> getAccount(
            @PathVariable Long customerNumber) {
        log.info("Received get account request for customerNumber: {}", customerNumber);
        GetAccountResponse response = accountService.getAccount(customerNumber);
        return ResponseEntity.status(HttpStatus.FOUND).body(response);
    }
}
