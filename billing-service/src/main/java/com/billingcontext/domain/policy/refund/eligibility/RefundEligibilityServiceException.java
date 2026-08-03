package com.billingcontext.domain.policy.refund.eligibility;

import com.billingcontext.domain.shared.DomainException;

public class RefundEligibilityServiceException extends DomainException {
    public RefundEligibilityServiceException(String message) {
        super(message);
    }
}
