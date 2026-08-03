package com.billingcontext.domain.policy.pricing.discount;

import com.billingcontext.domain.policy.pricing.discount.rules.DiscountRuleResult;
import com.billingcontext.domain.policy.pricing.discount.rules.DiscountRuleResultException;
import com.billingcontext.domain.policy.Evaluation;
import com.billingcontext.domain.shared.finance.Percentage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DiscountRuleResultTest {
    @Test
    void testDiscountRuleResultWithNullEvaluation(){
        Percentage percentage = Percentage.of(10);
        String reason = "Test reason";

        assertThrows(DiscountRuleResultException.class, () -> {
            new DiscountRuleResult(null, percentage, reason);
        });
    }
    @Test
    void testDiscountRuleResultWithEvaluationResultOfNeutral(){
        Percentage percentage = Percentage.of(10);
        String reason = "Test reason";

        assertThrows(DiscountRuleResultException.class, () -> {
            new DiscountRuleResult(Evaluation.NEUTRAL, percentage, reason);
        });
    }
    @Test
    void testDiscountRuleResultWithEvaluationResultOfApproveAndNullPercentage(){
        String reason = "Test reason";

        assertThrows(DiscountRuleResultException.class, () -> {
            new DiscountRuleResult(Evaluation.APPROVE, null, reason);
        });
    }
    @Test
    void testDiscountRuleResultWithEvaluationResultOfApproveAndNonNullReason(){
        String reason = "Test reason";
        assertThrows(DiscountRuleResultException.class, () -> {
            new DiscountRuleResult(Evaluation.APPROVE, Percentage.of(10), reason);
        });
    }
    @Test
    void testDiscountRuleResultWithEvaluationResultOfDenyAndNotNullPercentage(){
        String reason = "Test reason";
        assertThrows(DiscountRuleResultException.class, () -> {
            new DiscountRuleResult(Evaluation.DENY, Percentage.of(10), reason);
        });
    }
    @Test
    void testDiscountRuleResultWithEvaluationResultOfDenyAndNullReason(){
        assertThrows(DiscountRuleResultException.class, () -> {
            new DiscountRuleResult(Evaluation.DENY, null, null);
        });
    }
    @Test
    void testDiscountRuleResultWhenRuleEvaluationIsApproved(){
        Percentage percentage = Percentage.of(10);

        DiscountRuleResult discountRuleResult = DiscountRuleResult.applicable(percentage);

        assertEquals(Evaluation.APPROVE, discountRuleResult.status());
        assertEquals(percentage, discountRuleResult.percentage());
        assertNull(discountRuleResult.reason());
    }
    @Test
    void testDiscountRuleResultWhenRuleEvaluationIsDeny(){
        String reason = "Test reason";

        DiscountRuleResult discountRuleResult = DiscountRuleResult.notApplicable(reason);

        assertEquals(Evaluation.DENY, discountRuleResult.status());
        assertNull(discountRuleResult.percentage());
        assertEquals(reason, discountRuleResult.reason());
    }
    @Test
    void testIsApplicableWhenRuleEvaluationIsApproved(){
        Percentage percentage = Percentage.of(10);
        DiscountRuleResult discountRuleResult = DiscountRuleResult.applicable(percentage);

        assertTrue(discountRuleResult.isApplicable());
    }
    @Test
    void testIsApplicableWhenRuleEvaluationIsDeny(){
        String reason = "Test reason";
        DiscountRuleResult discountRuleResult = DiscountRuleResult.notApplicable(reason);

        assertFalse(discountRuleResult.isApplicable());
    }
}
