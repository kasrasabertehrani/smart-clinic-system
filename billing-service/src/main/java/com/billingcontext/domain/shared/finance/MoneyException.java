package com.billingcontext.domain.shared.finance;

import com.billingcontext.domain.shared.DomainException;

public class MoneyException extends DomainException {
    public MoneyException(String message) {
        super(message);
    }
}
