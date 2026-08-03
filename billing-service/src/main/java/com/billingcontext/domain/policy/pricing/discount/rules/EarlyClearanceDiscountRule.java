package com.billingcontext.domain.policy.pricing.discount.rules;


import com.billingcontext.domain.shared.finance.Percentage;
import com.billingcontext.domain.policy.pricing.evaluation.PricingEvaluationContext;

public class EarlyClearanceDiscountRule implements DiscountRule {

    private static final Percentage DISCOUNT = Percentage.of(3);
    private static final int MINIMUM_EARLY_MINUTES = 3 * 24 * 60; // 3 days

    @Override
    public DiscountRuleResult evaluate(PricingEvaluationContext context) {
        if (context.minutesToPaymentDueDate() >= MINIMUM_EARLY_MINUTES) {
            return DiscountRuleResult.applicable(DISCOUNT);
        }
        return DiscountRuleResult.notApplicable("Payment does not meet the 3-day early clearance threshold.");
    }

    @Override
    public DiscountType type() {
        return DiscountType.STACKABLE;
    }
}
