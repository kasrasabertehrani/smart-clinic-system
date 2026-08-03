package com.billingcontext.domain.policy.refund;

import com.billingcontext.domain.shared.DomainException;

public class RefundPolicyResultException extends DomainException {
    public RefundPolicyResultException(String message) {
        super(message);
    }
}
