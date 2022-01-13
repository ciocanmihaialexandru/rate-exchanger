package com.rate.exchanger.exception;

public class BankAccountNotFoundException extends RuntimeException {
    public BankAccountNotFoundException(String iban) {
        super(String.format("Bank account with iban %s not found", iban), new Throwable());
    }
}
