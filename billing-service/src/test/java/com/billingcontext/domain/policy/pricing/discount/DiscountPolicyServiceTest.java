package com.billingcontext.domain.policy.pricing.discount;

import com.billingcontext.domain.TestFixtures;
import com.billingcontext.domain.policy.pricing.discount.rules.DiscountRule;
import com.billingcontext.domain.policy.pricing.evaluation.PricingEvaluationContext;
import com.billingcontext.domain.shared.finance.Percentage;
import org.junit.jupiter.api.Test;


import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DiscountPolicyServiceTest {

    @Test
    void testCreationOfDiscountPolicyService(){
        List<DiscountRule> discountRules = TestFixtures.createDiscountRules();

        DiscountPolicyService discountPolicyService = new DiscountPolicyService(discountRules);

        assertEquals(2, discountPolicyService.getStackableRules().size());
        assertEquals(1, discountPolicyService.getExclusiveRules().size());
    }
    @Test
    void testEvaluateDiscountPolicyServiceRulesWhenExclusiveRuleDoesNotApply(){
        List<DiscountRule> discountRules = TestFixtures.createDiscountRules();
        DiscountPolicyService discountPolicyService = new DiscountPolicyService(discountRules);
        PricingEvaluationContext context = TestFixtures.createPricingEvaluationContext(110, 5000);

        Percentage discount = discountPolicyService.evaluate(context);

        assertEquals(Percentage.of(5.0), discount);
    }
    @Test
    void testEvaluateDiscountPolicyServiceWhenExclusiveRuleApplies(){
        List<DiscountRule> discountRules = TestFixtures.createDiscountRules();
        DiscountPolicyService discountPolicyService = new DiscountPolicyService(discountRules);
        PricingEvaluationContext context = TestFixtures.createPricingEvaluationContext(10, 5000);

        Percentage discount = discountPolicyService.evaluate(context);

        assertEquals(Percentage.of(10.0), discount);
    }
    @Test
    void testEvaluateDiscountPolicyServiceWhenOneStackableRuleApplies(){
        List<DiscountRule> discountRules = TestFixtures.createDiscountRules();
        DiscountPolicyService discountPolicyService = new DiscountPolicyService(discountRules);
        PricingEvaluationContext context = TestFixtures.createPricingEvaluationContext(20, 3000);

        Percentage discount = discountPolicyService.evaluate(context);

        assertEquals(Percentage.of(2.0), discount);
    }

}
