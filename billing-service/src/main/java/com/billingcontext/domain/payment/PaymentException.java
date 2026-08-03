package com.billingcontext.domain.payment;

public class PaymentException extends RuntimeException {
    public PaymentException(String message) {
        super(message);
    }
}
