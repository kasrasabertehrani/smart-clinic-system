package com.smartclinicsystem.domain.exception;

public class NotSharpTimeException extends DomainException {
    public NotSharpTimeException() {
        super("Time must be exactly on the hour (00 minutes, 00 seconds).");
    }
}
