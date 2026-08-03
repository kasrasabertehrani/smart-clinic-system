package com.billingcontext.domain.payment;

import com.billingcontext.domain.shared.DomainException;

public class PaymentResultException extends DomainException {
    public PaymentResultException(String message) {
        super(message);
    }
}
