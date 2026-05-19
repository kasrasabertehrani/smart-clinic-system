package com.smartclinicsystem.domain.exception;

public class OutsideWorkingHoursException extends DomainException {
    public OutsideWorkingHoursException(String message) {
        super(message);
    }
}