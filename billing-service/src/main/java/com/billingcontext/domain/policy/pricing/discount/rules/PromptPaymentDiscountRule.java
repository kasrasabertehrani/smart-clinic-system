package com.billingcontext.domain.policy.pricing.discount.rules;


import com.billingcontext.domain.shared.finance.Percentage;
import com.billingcontext.domain.policy.pricing.evaluation.PricingEvaluationContext;

public class PromptPaymentDiscountRule implements DiscountRule {

    private static final Percentage DISCOUNT = Percentage.of(2);
    private static final int MAX_MINUTES = 120;

    @Override
    public DiscountRuleResult evaluate(PricingEvaluationContext context) {
        if (context.minutesAfterProformaCreation() <= MAX_MINUTES) {
            return DiscountRuleResult.applicable(DISCOUNT);
        }
        return DiscountRuleResult.notApplicable("Payment was made after the 2-hour prompt window.");
    }

    @Override
    public DiscountType type() {
        return DiscountType.STACKABLE;
    }
}
