package com.billingcontext.domain.policy.pricing.discount.rules;

import com.billingcontext.domain.shared.DomainException;

public class DiscountRuleResultException extends DomainException {
    public DiscountRuleResultException(String message) {
        super(message);
    }
}
