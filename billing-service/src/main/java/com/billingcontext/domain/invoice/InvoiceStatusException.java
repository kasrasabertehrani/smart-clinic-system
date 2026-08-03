package com.billingcontext.domain.invoice;

public class InvoiceStatusException extends RuntimeException {
    public InvoiceStatusException(String message) {
        super(message);
    }
}
