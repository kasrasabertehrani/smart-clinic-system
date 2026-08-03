package com.billingcontext.domain.policy.pricing;

import com.billingcontext.domain.TestFixtures;
import com.billingcontext.domain.policy.pricing.discount.DiscountPolicyService;
import com.billingcontext.domain.policy.pricing.evaluation.PricingEvaluationContext;
import com.billingcontext.domain.shared.finance.Money;
import com.billingcontext.domain.shared.finance.Percentage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class PricingPolicyServiceTest {
    @Test
    void testPricingPolicyServiceCreation(){
        PricingEvaluationContext pricingEvaluationContext = TestFixtures.createPricingEvaluationContext(
                20,5000);
        DiscountPolicyService discountPolicyService = TestFixtures.createDiscountPolicyService();

        PricingPolicyService pricingPolicyService = new PricingPolicyService(discountPolicyService);

        assertNotNull(pricingPolicyService);

    }
    @Test
    void testCalculateFinalPrice(){
        PricingEvaluationContext pricingEvaluationContext = TestFixtures.createPricingEvaluationContext(
                10,5000);
        DiscountPolicyService discountPolicyService = TestFixtures.createDiscountPolicyService();
        PricingPolicyService pricingPolicyService = new PricingPolicyService(discountPolicyService);

        PricingPolicyResult result = pricingPolicyService.calculateFinalPrice(pricingEvaluationContext);

        assertEquals(Money.moneyEUR(100.00), result.basePrice());
        assertEquals(Percentage.of(10.0), result.discountPercentage());
        assertEquals(Percentage.of(10.0), result.taxPercentage());
        assertEquals(Money.moneyEUR(99.00), result.finalPrice());
    }
}
