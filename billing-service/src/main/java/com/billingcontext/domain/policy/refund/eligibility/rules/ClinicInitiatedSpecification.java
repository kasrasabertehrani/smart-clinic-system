package com.billingcontext.domain.policy.refund.eligibility.rules;


import com.billingcontext.domain.policy.refund.evaluation.RefundEvaluationContext;

public class ClinicInitiatedSpecification implements RefundEligibilityRule {

    @Override
    public RuleResult evaluate(RefundEvaluationContext context) {
        if (context.isCanceledByClinicStaff() || context.isCanceledBySystemAutomation()) {
            return RuleResult.forceApprove();
        }
        return RuleResult.neutral();
    }

    @Override
    public RuleOrder order() {
        return RuleOrder.override(1);
    }
}
