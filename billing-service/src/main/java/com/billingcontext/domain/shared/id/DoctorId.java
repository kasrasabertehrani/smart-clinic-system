package com.billingcontext.domain.shared.id;

public record DoctorId(String value) {
    public DoctorId {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Patient ID cannot be null or blank");
        }
    }
}