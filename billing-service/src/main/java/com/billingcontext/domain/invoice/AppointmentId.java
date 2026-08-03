package com.billingcontext.domain.invoice;

public record AppointmentId(String value) {
    public AppointmentId {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Patient ID cannot be null or blank");
        }
    }
}