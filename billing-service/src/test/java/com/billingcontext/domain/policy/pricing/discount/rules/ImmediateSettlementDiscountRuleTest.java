package com.billingcontext.domain.policy.pricing.discount.rules;

import com.billingcontext.domain.TestFixtures;
import com.billingcontext.domain.policy.pricing.evaluation.PricingEvaluationContext;
import com.billingcontext.domain.policy.Evaluation;
import com.billingcontext.domain.shared.finance.Percentage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class ImmediateSettlementDiscountRuleTest {

    @Test
    void testEvaluateRuleWhenProformaWasCreatedLessThanFifteenMinutes(){
        PricingEvaluationContext pricingEvaluationContext =
                TestFixtures.createPricingEvaluationContext(10, 5000);
        DiscountRule immediateSettlementDiscountRuleTest = new ImmediateSettlementDiscountRule();

        DiscountRuleResult result = immediateSettlementDiscountRuleTest.evaluate(pricingEvaluationContext);
        DiscountType ruleType = immediateSettlementDiscountRuleTest.type();

        assertEquals(Evaluation.APPROVE, result.status());
        assertEquals(Percentage.of(10.00), result.percentage());
        assertNull(result.reason());
        assertEquals(DiscountType.EXCLUSIVE, ruleType);
    }
    @Test
    void testEvaluateRuleWhenProformaWasCreatedMoreThanFifteenMinutesAgo(){
        PricingEvaluationContext pricingEvaluationContext =
                TestFixtures.createPricingEvaluationContext(20, 5000);
        DiscountRule immediateSettlementDiscountRuleTest = new ImmediateSettlementDiscountRule();

        DiscountRuleResult result = immediateSettlementDiscountRuleTest.evaluate(pricingEvaluationContext);
        DiscountType ruleType = immediateSettlementDiscountRuleTest.type();

        assertEquals(Evaluation.DENY, result.status());
        assertNull(result.percentage());
        assertEquals("Outside the 15-minute immediate settlement window.", result.reason());
        assertEquals(DiscountType.EXCLUSIVE, ruleType);
    }
}
