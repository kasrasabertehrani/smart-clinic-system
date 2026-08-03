package com.billingcontext.domain.invoice;

import com.billingcontext.domain.shared.DomainEvent;

import java.time.Instant;

public record InvoiceWasExpired (AppointmentId appointmentId, Instant occurredOn) implements DomainEvent {

    public InvoiceWasExpired(AppointmentId appointmentId) {
        this(appointmentId, Instant.now());
    }

}

