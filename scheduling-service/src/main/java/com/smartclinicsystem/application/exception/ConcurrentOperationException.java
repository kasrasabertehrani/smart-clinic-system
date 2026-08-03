package com.smartclinicsystem.application.exception;

public class ConcurrentOperationException extends RuntimeException {
    public ConcurrentOperationException(String message) {
        super(message);
    }
}
