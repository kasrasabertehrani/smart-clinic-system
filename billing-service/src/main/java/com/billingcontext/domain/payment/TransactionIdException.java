package com.billingcontext.domain.payment;

import com.billingcontext.domain.shared.DomainException;

public class TransactionIdException extends DomainException {
    public TransactionIdException(String message) {
        super(message);
    }
}
