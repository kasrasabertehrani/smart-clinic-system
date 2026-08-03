package com.billingcontext.domain.policy.pricing.discount.rules;

import com.billingcontext.domain.TestFixtures;
import com.billingcontext.domain.policy.pricing.evaluation.PricingEvaluationContext;
import com.billingcontext.domain.policy.Evaluation;
import com.billingcontext.domain.shared.finance.Percentage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class EarlyClearanceDiscountRuleTest {

    @Test
    void testEvaluateRuleWhenPatientPaidEarlierThanThreeDays() {
        PricingEvaluationContext pricingEvaluationContext =
                TestFixtures.createPricingEvaluationContext(30, 5000);
        DiscountRule earlyClearanceDiscountRule = new EarlyClearanceDiscountRule();

        DiscountRuleResult result = earlyClearanceDiscountRule.evaluate(pricingEvaluationContext);
        DiscountType ruleType = earlyClearanceDiscountRule.type();

        assertEquals(Evaluation.APPROVE, result.status());
        assertEquals(Percentage.of(3.00), result.percentage());
        assertNull(result.reason());
        assertEquals(DiscountType.STACKABLE, ruleType);
    }
    @Test
    void testEvaluateDiscountRuleWhenPatientPaidLessThanThreeDays() {
        PricingEvaluationContext pricingEvaluationContext =
                TestFixtures.createPricingEvaluationContext(30, 1000);
        DiscountRule earlyClearanceDiscountRule = new EarlyClearanceDiscountRule();

        DiscountRuleResult result = earlyClearanceDiscountRule.evaluate(pricingEvaluationContext);

        assertEquals(Evaluation.DENY, result.status());
        assertNull(result.percentage());
        assertEquals("Payment does not meet the 3-day early clearance threshold.", result.reason());
    }

}
