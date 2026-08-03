package com.billingcontext.domain.payment;


import com.billingcontext.domain.shared.DomainEvent;
import com.billingcontext.domain.invoice.InvoiceId;
import com.billingcontext.domain.shared.finance.Money;


import java.time.Instant;

public record PaymentWasSuccessful(InvoiceId invoiceId, Money amountOwed, Instant occurredOn) implements DomainEvent {

    public PaymentWasSuccessful(InvoiceId invoiceId,  Money amountOwed) {
        this(invoiceId, amountOwed, Instant.now());
    }
}

