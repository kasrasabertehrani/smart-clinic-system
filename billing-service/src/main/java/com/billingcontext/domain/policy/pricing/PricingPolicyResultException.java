package com.billingcontext.domain.policy.pricing;

import com.billingcontext.domain.shared.DomainException;

public class PricingPolicyResultException extends DomainException {
    public PricingPolicyResultException(String message) {
        super(message);
    }
}
