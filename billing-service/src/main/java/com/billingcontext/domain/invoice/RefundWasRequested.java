package com.billingcontext.domain.invoice;

import com.billingcontext.domain.shared.DomainEvent;
import com.billingcontext.domain.shared.finance.Money;
import com.billingcontext.domain.shared.id.PatientId;

import java.time.Instant;

public record RefundWasRequested (
    InvoiceId invoiceId,
    PatientId patientId,
    Money refundAmount,
    Instant occurredOn
) implements DomainEvent {

    public RefundWasRequested(InvoiceId invoiceId, PatientId patientId, Money refundAmount) {
            this(invoiceId, patientId, refundAmount, Instant.now());
        }
    }
