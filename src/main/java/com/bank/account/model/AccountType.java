package com.bank.account.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum AccountType {

    SAVINGS("S"),
    CHECKING("C");

    private final String code;

    AccountType(String code) {
        this.code = code;
    }

    @JsonValue
    public String getCode() {
        return code;
    }

    @JsonCreator
    public static AccountType fromValue(String value) {
        for (AccountType type : values()) {
            if (type.code.equalsIgnoreCase(value) || type.name().equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException(
                "Invalid accountType '" + value + "'. Accepted values: S (Savings), C (Checking)");
    }
}
