package com.billingcontext.domain.policy.pricing.discount.rules;


import com.billingcontext.domain.shared.finance.Percentage;
import com.billingcontext.domain.policy.pricing.evaluation.PricingEvaluationContext;

public class ImmediateSettlementDiscountRule implements DiscountRule {

    private static final Percentage DISCOUNT = Percentage.of(10);
    private static final int FLASH_SALE_MINUTES = 15;

    @Override
    public DiscountRuleResult evaluate(PricingEvaluationContext context) {
        if (context.minutesAfterProformaCreation() <= FLASH_SALE_MINUTES) {
            return DiscountRuleResult.applicable(DISCOUNT);
        }
        return DiscountRuleResult.notApplicable("Outside the 15-minute immediate settlement window.");
    }

    @Override
    public DiscountType type() {
        return DiscountType.EXCLUSIVE;
    }
}
