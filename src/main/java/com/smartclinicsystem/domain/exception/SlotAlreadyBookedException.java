package com.smartclinicsystem.domain.exception;

public class SlotAlreadyBookedException extends DomainException {
    public SlotAlreadyBookedException(String message) {
        super(message);
    }
}