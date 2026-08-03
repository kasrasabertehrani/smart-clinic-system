package com.billingcontext.domain.policy.refund.eligibility.rules;


import com.billingcontext.domain.policy.refund.evaluation.RefundEvaluationContext;


public class MinimumNoticeSpecification implements RefundEligibilityRule {

    private static final int MINIMUM_HOURS_NOTICE = 24;


    @Override
    public RuleResult evaluate(RefundEvaluationContext context) {
        long hoursNotice = context.minutesUntilAppointment() / 60;

        if (context.isCanceledByPatient() && hoursNotice < MINIMUM_HOURS_NOTICE) {
            return RuleResult.deny("Patient canceled with less than 24 hours notice.");
        }
        return RuleResult.neutral();
    }

    @Override
    public RuleOrder order() {
        return RuleOrder.standard(1);
    }


}