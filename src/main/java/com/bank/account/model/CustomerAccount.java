package com.bank.account.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "customer_account")
public class CustomerAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "customer_name", nullable = false, length = 50)
    private String customerName;

    @Column(name = "customer_mobile", nullable = false, length = 20)
    private String customerMobile;

    @Column(name = "customer_email", nullable = false, length = 50)
    private String customerEmail;

    @Column(name = "address1", nullable = false, length = 100)
    private String address1;

    @Column(name = "address2", length = 100)
    private String address2;

    @Enumerated(EnumType.STRING)
    @Column(name = "account_type", nullable = false, length = 10)
    private AccountType accountType;

    @Column(name = "account_number", nullable = false, unique = true)
    private Long accountNumber;

    @Column(name = "available_balance", nullable = false, precision = 15, scale = 2)
    private BigDecimal availableBalance;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        // Generate a simple account number if not set
        if (this.accountNumber == null) {
            this.accountNumber = System.currentTimeMillis() % 1_000_000_000L + 10000L;
        }
        if (this.availableBalance == null) {
            this.availableBalance = BigDecimal.ZERO;
        }
    }

    public Long getId()                             { return id; }
    public void setId(Long id)                      { this.id = id; }

    public String getCustomerName()                 { return customerName; }
    public void setCustomerName(String v)           { this.customerName = v; }

    public String getCustomerMobile()               { return customerMobile; }
    public void setCustomerMobile(String v)         { this.customerMobile = v; }

    public String getCustomerEmail()                { return customerEmail; }
    public void setCustomerEmail(String v)          { this.customerEmail = v; }

    public String getAddress1()                     { return address1; }
    public void setAddress1(String v)               { this.address1 = v; }

    public String getAddress2()                     { return address2; }
    public void setAddress2(String v)               { this.address2 = v; }

    public AccountType getAccountType()             { return accountType; }
    public void setAccountType(AccountType v)       { this.accountType = v; }

    public Long getAccountNumber()                  { return accountNumber; }
    public void setAccountNumber(Long v)            { this.accountNumber = v; }

    public BigDecimal getAvailableBalance()         { return availableBalance; }
    public void setAvailableBalance(BigDecimal v)   { this.availableBalance = v; }

    public LocalDateTime getCreatedAt()             { return createdAt; }
    public void setCreatedAt(LocalDateTime v)       { this.createdAt = v; }
}
