package com.billingcontext.domain.invoice;

import com.billingcontext.domain.shared.DomainEvent;

import java.time.Instant;

public record InvoiceWasExpired (String appointmentId, Instant occurredOn) implements DomainEvent {

    public InvoiceWasExpired(String appointmentId) {
        this(appointmentId, Instant.now());
    }

}

