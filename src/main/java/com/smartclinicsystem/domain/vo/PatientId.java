package com.smartclinicsystem.domain.vo;

public record PatientId(String value) {
    public PatientId {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Patient ID cannot be null or blank");
        }
    }
}
