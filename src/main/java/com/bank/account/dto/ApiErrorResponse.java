package com.bank.account.dto;

public class ApiErrorResponse {

    private int transactionStatusCode;
    private String transactionStatusDescription;

    private ApiErrorResponse() {}

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private final ApiErrorResponse obj = new ApiErrorResponse();

        public Builder transactionStatusCode(int v)             { obj.transactionStatusCode = v; return this; }
        public Builder transactionStatusDescription(String v)   { obj.transactionStatusDescription = v; return this; }
        public ApiErrorResponse build()                         { return obj; }
    }

    public int getTransactionStatusCode()            { return transactionStatusCode; }
    public String getTransactionStatusDescription()  { return transactionStatusDescription; }
}
