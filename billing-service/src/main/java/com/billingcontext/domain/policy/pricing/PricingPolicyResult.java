package com.billingcontext.domain.policy.pricing;

import com.billingcontext.domain.shared.finance.Money;
import com.billingcontext.domain.shared.finance.Percentage;




public record PricingPolicyResult(
        Money basePrice,
        Percentage discountPercentage,
        Percentage taxPercentage,
        Money finalPrice
) {
    public PricingPolicyResult {
        if(basePrice.isZero()) {
            throw new PricingPolicyResultException("Base Price cannot be zero");
        }
        if(discountPercentage.isHundred() && !finalPrice.isZero()) {
            throw new PricingPolicyResultException("Final Price must be zero when discount is 100%");
        }
        if(discountPercentage.isZero() && basePrice.isGreaterThanOrEqualTo(finalPrice)) {
            throw new PricingPolicyResultException("Base Price cannot be greater than Final Price When discount is 0%");
        }

        Money discountedPrice = discountPercentage.applyDiscountTo(basePrice);
        Money expectedFinalPrice = taxPercentage.applyTaxTo(discountedPrice);

        if (!expectedFinalPrice.equals(finalPrice)) {
            throw new PricingPolicyResultException(
                    "Final price is not valid!");
        }
    }
    public boolean isFree() {
        return finalPrice.amount().signum() == 0;
    }

}


