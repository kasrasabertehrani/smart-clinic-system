package com.billingcontext.domain.invoice;

public record InvoiceId(String value) {
    public InvoiceId {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Patient ID cannot be null or blank");
        }
    }
}