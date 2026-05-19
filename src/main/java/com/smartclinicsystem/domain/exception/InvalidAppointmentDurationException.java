package com.smartclinicsystem.domain.exception;

public class InvalidAppointmentDurationException extends DomainException {
    public InvalidAppointmentDurationException() {
        super("Duration of the appointment must be exactly one hour.");
    }
}
