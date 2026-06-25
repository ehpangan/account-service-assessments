package com.bank.account.dto;

import com.bank.account.model.AccountType;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class CreateAccountRequest {

    @NotBlank(message = "customerName is required")
    @Size(max = 50, message = "customerName must not exceed 50 characters")
    private String customerName;

    @NotBlank(message = "customerMobile is required")
    @Size(max = 20, message = "customerMobile must not exceed 20 characters")
    private String customerMobile;

    @NotBlank(message = "customerEmail is required")
    @Email(message = "customerEmail must be a valid email address")
    @Size(max = 50, message = "customerEmail must not exceed 50 characters")
    private String customerEmail;

    @NotBlank(message = "address1 is required")
    @Size(max = 100, message = "address1 must not exceed 100 characters")
    private String address1;

    @Size(max = 100, message = "address2 must not exceed 100 characters")
    private String address2;

    @NotNull(message = "accountType is required (S = Savings, C = Checking)")
    private AccountType accountType;

    // Getters and Setters
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getCustomerMobile() { return customerMobile; }
    public void setCustomerMobile(String customerMobile) { this.customerMobile = customerMobile; }

    public String getCustomerEmail() { return customerEmail; }
    public void setCustomerEmail(String customerEmail) { this.customerEmail = customerEmail; }

    public String getAddress1() { return address1; }
    public void setAddress1(String address1) { this.address1 = address1; }

    public String getAddress2() { return address2; }
    public void setAddress2(String address2) { this.address2 = address2; }

    public AccountType getAccountType() { return accountType; }
    public void setAccountType(AccountType accountType) { this.accountType = accountType; }
}
