package com.billingcontext.domain.policy.pricing.evaluation;

import com.billingcontext.domain.shared.DomainException;

public class PricingEvaluationContextException extends DomainException {
    public PricingEvaluationContextException(String message) {
        super(message);
    }
}
