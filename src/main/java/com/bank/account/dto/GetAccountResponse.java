package com.bank.account.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Response payload for GET /api/v1/account/{customerNumber}
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GetAccountResponse {

    private Long customerNumber;
    private String customerName;
    private String customerMobile;
    private String customerEmail;
    private String address1;
    private String address2;

    // Dynamically populated based on account type
    private List<AccountDetailDto> savings;
    private List<AccountDetailDto> checking;

    private int transactionStatusCode;
    private String transactionStatusDescription;

    private GetAccountResponse() {}

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private final GetAccountResponse obj = new GetAccountResponse();

        public Builder customerNumber(Long v)                   { obj.customerNumber = v; return this; }
        public Builder customerName(String v)                   { obj.customerName = v; return this; }
        public Builder customerMobile(String v)                 { obj.customerMobile = v; return this; }
        public Builder customerEmail(String v)                  { obj.customerEmail = v; return this; }
        public Builder address1(String v)                       { obj.address1 = v; return this; }
        public Builder address2(String v)                       { obj.address2 = v; return this; }
        public Builder savings(List<AccountDetailDto> v)        { obj.savings = v; return this; }
        public Builder checking(List<AccountDetailDto> v)       { obj.checking = v; return this; }
        public Builder transactionStatusCode(int v)             { obj.transactionStatusCode = v; return this; }
        public Builder transactionStatusDescription(String v)   { obj.transactionStatusDescription = v; return this; }
        public GetAccountResponse build()                       { return obj; }
    }

    public Long getCustomerNumber()                 { return customerNumber; }
    public String getCustomerName()                 { return customerName; }
    public String getCustomerMobile()               { return customerMobile; }
    public String getCustomerEmail()                { return customerEmail; }
    public String getAddress1()                     { return address1; }
    public String getAddress2()                     { return address2; }
    public List<AccountDetailDto> getSavings()      { return savings; }
    public List<AccountDetailDto> getChecking()     { return checking; }
    public int getTransactionStatusCode()           { return transactionStatusCode; }
    public String getTransactionStatusDescription() { return transactionStatusDescription; }
}
