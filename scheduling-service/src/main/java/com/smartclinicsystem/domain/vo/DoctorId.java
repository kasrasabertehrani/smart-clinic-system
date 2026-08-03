package com.smartclinicsystem.domain.vo;

public record DoctorId(String value) {

    public DoctorId {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Doctor ID cannot be null or blank");
        }
    }
}
