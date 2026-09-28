package com.billingcontext.application.usecase.payment.initiate.checkout;

import com.billingcontext.domain.shared.DomainException;

public class CheckoutAlreadyPendingException extends DomainException {
    public CheckoutAlreadyPendingException(String message) {
        super(message);
    }
}
