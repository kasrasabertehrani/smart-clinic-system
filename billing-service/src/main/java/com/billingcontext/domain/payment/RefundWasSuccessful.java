package com.billingcontext.domain.payment;

import com.billingcontext.domain.invoice.InvoiceId;
import com.billingcontext.domain.shared.DomainEvent;
import com.billingcontext.domain.shared.finance.Money;

import java.time.Instant;

public record RefundWasSuccessful(InvoiceId invoiceId, Money amountOwed, Instant occurredOn) implements DomainEvent {

    public RefundWasSuccessful(InvoiceId invoiceId,  Money amountOwed) {
        this(invoiceId, amountOwed, Instant.now());
    }
}
