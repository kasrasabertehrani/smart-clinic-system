package com.billingcontext.domain.shared.finance;

import com.billingcontext.domain.shared.DomainException;

public class PercentageException extends DomainException {
    public PercentageException(String message) {
        super(message);
    }
}
