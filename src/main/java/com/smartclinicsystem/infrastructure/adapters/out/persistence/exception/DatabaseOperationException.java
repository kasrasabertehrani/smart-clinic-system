package com.smartclinicsystem.infrastructure.adapters.out.persistence.exception;

public class DatabaseOperationException extends RuntimeException {
    public DatabaseOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}
