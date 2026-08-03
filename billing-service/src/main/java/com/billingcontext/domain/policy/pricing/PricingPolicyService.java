package com.billingcontext.domain.policy.pricing;

import com.billingcontext.domain.policy.pricing.discount.DiscountPolicyService;
import com.billingcontext.domain.policy.pricing.evaluation.PricingEvaluationContext;
import com.billingcontext.domain.shared.finance.Money;
import com.billingcontext.domain.shared.finance.Percentage;

public record PricingPolicyService(DiscountPolicyService discountPolicyService) {

    private static final Percentage TAX_RATE = Percentage.of(10.0);

    public PricingPolicyResult calculateFinalPrice(PricingEvaluationContext pricingEvaluationContext) {
        Money basePrice = pricingEvaluationContext.basePrice();
        Percentage discountPercentage = discountPolicyService.evaluate(pricingEvaluationContext);
        Money totalBeforeTax = discountPercentage.applyDiscountTo(basePrice);
        Money totalAfterTax = TAX_RATE.applyTaxTo(totalBeforeTax);
        return new PricingPolicyResult(basePrice, discountPercentage, TAX_RATE, totalAfterTax);
    }
}
