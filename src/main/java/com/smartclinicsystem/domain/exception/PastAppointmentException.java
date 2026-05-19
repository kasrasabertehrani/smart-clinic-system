package com.smartclinicsystem.domain.exception;

public class PastAppointmentException extends DomainException {
    public PastAppointmentException() {
        super("Appointment date and time must be in the future.");
    }
}
