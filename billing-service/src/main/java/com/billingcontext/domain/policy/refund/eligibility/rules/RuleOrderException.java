package com.billingcontext.domain.policy.refund.eligibility.rules;

import com.billingcontext.domain.shared.DomainException;

public class RuleOrderException extends DomainException {
    public RuleOrderException(String message) {
        super(message);
    }
}
