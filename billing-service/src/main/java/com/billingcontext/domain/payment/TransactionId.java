package com.billingcontext.domain.payment;

public record TransactionId(String value) {
    public TransactionId {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Patient ID cannot be null or blank");
        }
    }
}