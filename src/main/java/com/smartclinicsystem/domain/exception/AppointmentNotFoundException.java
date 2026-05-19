package com.smartclinicsystem.domain.exception;

public class AppointmentNotFoundException extends DomainException {
    public AppointmentNotFoundException(String message) {
        super(message);
    }
}