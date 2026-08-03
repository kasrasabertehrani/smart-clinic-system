package com.billingcontext.domain.policy.pricing.discount.rules;

import com.billingcontext.domain.policy.pricing.evaluation.PricingEvaluationContext;

public interface DiscountRule {
    DiscountRuleResult evaluate(PricingEvaluationContext context);
    DiscountType type();
}