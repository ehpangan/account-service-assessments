package com.bank.account.dto;

public class CreateAccountResponse {

    private Long customerNumber;
    private int transactionStatusCode;
    private String transactionStatusDescription;

    private CreateAccountResponse() {}

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private final CreateAccountResponse obj = new CreateAccountResponse();

        public Builder customerNumber(Long v)                    { obj.customerNumber = v; return this; }
        public Builder transactionStatusCode(int v)              { obj.transactionStatusCode = v; return this; }
        public Builder transactionStatusDescription(String v)    { obj.transactionStatusDescription = v; return this; }
        public CreateAccountResponse build()                     { return obj; }
    }

    public Long getCustomerNumber()                  { return customerNumber; }
    public int getTransactionStatusCode()            { return transactionStatusCode; }
    public String getTransactionStatusDescription()  { return transactionStatusDescription; }
}
