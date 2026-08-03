package com.billingcontext.infrastructure.persistence.exception;

public class DatabaseOperationException extends RuntimeException {
    public DatabaseOperationException(String message) {
        super(message);
    }
}
