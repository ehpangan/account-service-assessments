package com.bank.account.dto;

import java.math.BigDecimal;

/**
 * Represents a single account entry nested inside the GET customer response.
 * e.g. under "savings" or "checking" array.
 */
public class AccountDetailDto {

    private final Long accountNumber;
    private final String accountType;
    private final BigDecimal availableBalance;

    public AccountDetailDto(Long accountNumber, String accountType, BigDecimal availableBalance) {
        this.accountNumber = accountNumber;
        this.accountType = accountType;
        this.availableBalance = availableBalance;
    }

    public Long getAccountNumber()          { return accountNumber; }
    public String getAccountType()          { return accountType; }
    public BigDecimal getAvailableBalance() { return availableBalance; }
}
