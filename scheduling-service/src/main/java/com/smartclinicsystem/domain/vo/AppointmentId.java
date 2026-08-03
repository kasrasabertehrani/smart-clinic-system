package com.smartclinicsystem.domain.vo;

public record AppointmentId(String value) {
    public AppointmentId {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Appointment ID cannot be null or blank");
        }
    }
}
