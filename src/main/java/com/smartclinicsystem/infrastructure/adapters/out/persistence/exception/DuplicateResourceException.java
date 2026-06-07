package com.smartclinicsystem.infrastructure.adapters.out.persistence.exception;

public class DuplicateResourceException extends RuntimeException {
    public DuplicateResourceException(String message) {
        super(message);
    }
}
