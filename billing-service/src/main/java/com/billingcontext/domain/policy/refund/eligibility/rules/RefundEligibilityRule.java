package com.billingcontext.domain.policy.refund.eligibility.rules;


import com.billingcontext.domain.policy.refund.evaluation.RefundEvaluationContext;


public interface RefundEligibilityRule {

    RuleResult evaluate(RefundEvaluationContext context);

    RuleOrder order();
}