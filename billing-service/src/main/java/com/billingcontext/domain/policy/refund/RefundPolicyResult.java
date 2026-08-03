package com.billingcontext.domain.policy.refund;


import com.billingcontext.domain.shared.finance.Money;

import java.util.Currency;

public record RefundPolicyResult(
        boolean isEligible,
        Money refundAmount,
        String rejectionReason
) {
    public RefundPolicyResult {
        if(refundAmount == null) {
            throw new RefundPolicyResultException("refundAmount is null");
        }
        if(isEligible && rejectionReason != null) {
            throw new RefundPolicyResultException("An approved refund cannot have a rejection reason.");
        }
        if(!isEligible  && rejectionReason == null) {
            throw new RefundPolicyResultException("A denied refund must have a rejection reason.");
        }
        if (!isEligible && !refundAmount.isZero()) {
            throw new RefundPolicyResultException("A denied refund cannot have a monetary value greater than zero.");
        }
        if (isEligible && refundAmount.isZero()) {
            throw new RefundPolicyResultException("An approved refund must specify the approved category.");
        }
    }

    public static RefundPolicyResult approved(Money amount) {
        return new RefundPolicyResult(true, amount, null);
    }

    public static RefundPolicyResult denied(String reason, Currency currency) {
        return new RefundPolicyResult(false, Money.zero(currency), reason);
    }

}
