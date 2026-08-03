package com.billingcontext.domain.policy.refund.evaluation;

import com.billingcontext.domain.shared.DomainException;

public class RefundEvaluationContextException extends DomainException {
    public RefundEvaluationContextException(String message) {
        super(message);
    }
}
