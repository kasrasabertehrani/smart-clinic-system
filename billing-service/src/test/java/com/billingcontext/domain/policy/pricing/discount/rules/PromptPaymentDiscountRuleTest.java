package com.billingcontext.domain.policy.pricing.discount.rules;

import com.billingcontext.domain.TestFixtures;
import com.billingcontext.domain.policy.pricing.evaluation.PricingEvaluationContext;
import com.billingcontext.domain.policy.Evaluation;
import com.billingcontext.domain.shared.finance.Percentage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class PromptPaymentDiscountRuleTest {
    @Test
    void testEvaluateRuleWhenPaymentIsWithinTwoHours() {
        PricingEvaluationContext pricingEvaluationContext =
                TestFixtures.createPricingEvaluationContext(110, 5000);
        DiscountRule promptPaymentDiscountRule = new PromptPaymentDiscountRule();

        DiscountRuleResult result = promptPaymentDiscountRule.evaluate(pricingEvaluationContext);
        DiscountType ruleType = promptPaymentDiscountRule.type();

        assertEquals(Evaluation.APPROVE, result.status());
        assertEquals(Percentage.of(2.00), result.percentage());
        assertNull(result.reason());
        assertEquals(DiscountType.STACKABLE, ruleType);
    }
    @Test
    void testEvaluationRuleWhenPaymentIsAfterTwoHours() {
        PricingEvaluationContext pricingEvaluationContext =
                TestFixtures.createPricingEvaluationContext(130, 5000);
        DiscountRule promptPaymentDiscountRule = new PromptPaymentDiscountRule();

        DiscountRuleResult result = promptPaymentDiscountRule.evaluate(pricingEvaluationContext);
        DiscountType ruleType = promptPaymentDiscountRule.type();

        assertEquals(Evaluation.DENY, result.status());
        assertNull(result.percentage());
        assertEquals("Payment was made after the 2-hour prompt window.", result.reason());
        assertEquals(DiscountType.STACKABLE, ruleType);
    }
}
