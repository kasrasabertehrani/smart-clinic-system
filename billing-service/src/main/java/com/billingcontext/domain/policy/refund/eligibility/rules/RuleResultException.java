package com.billingcontext.domain.policy.refund.eligibility.rules;

import com.billingcontext.domain.shared.DomainException;

public class RuleResultException extends DomainException {
    public RuleResultException(String message) {
        super(message);
    }
}
