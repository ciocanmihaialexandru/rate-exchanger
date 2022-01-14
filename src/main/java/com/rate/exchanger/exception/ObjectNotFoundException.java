package com.rate.exchanger.exception;

public class ObjectNotFoundException extends RuntimeException {
    public ObjectNotFoundException(String iban) {
        super(String.format("Bank account with iban %s not found", iban));
    }
}
