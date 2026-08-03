package com.billingcontext.domain.payment;

public class PaymentStatusException extends RuntimeException {
    public PaymentStatusException(String message) {
        super(message);
    }
}
