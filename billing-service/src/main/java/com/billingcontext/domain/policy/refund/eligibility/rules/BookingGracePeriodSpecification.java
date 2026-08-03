package com.billingcontext.domain.policy.refund.eligibility.rules;


import com.billingcontext.domain.policy.refund.evaluation.RefundEvaluationContext;

public class BookingGracePeriodSpecification implements RefundEligibilityRule {

    private static final int GRACE_PERIOD_MINUTES = 15;

    @Override
    public RuleResult evaluate(RefundEvaluationContext context) {
        if (context.isCanceledByPatient() && context.durationSinceBooking() < GRACE_PERIOD_MINUTES) {
            return RuleResult.forceApprove();
        }
        return RuleResult.neutral();

    }

    @Override
    public RuleOrder order() {
        return RuleOrder.override(2);
    }
}
