package com.billingcontext.domain.payment;


public record PaymentId(String value) {
    public PaymentId {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Patient ID cannot be null or blank");
        }
    }
}
