package com.billingcontext.domain.policy.refund.amount.strategy;

import com.billingcontext.domain.TestFixtures;
import com.billingcontext.domain.policy.refund.evaluation.RefundEvaluationContext;
import com.billingcontext.domain.shared.finance.Money;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DefaultStrategyTest {
    @Test
    void testDefaultStrategy() {
        RefundEvaluationContext refundEvaluationContext = TestFixtures.createRefundEvaluationContext(Money.moneyEUR(100.0),
                "patient", 100L,1000L);
        RefundCalculationStrategy refundCalculationStrategy = new DefaultStrategy();
        int priority = refundCalculationStrategy.priority();
        boolean appliesTo = refundCalculationStrategy.appliesTo(refundEvaluationContext);
        Money refundAmount = refundCalculationStrategy.calculateRefundAmount(refundEvaluationContext);

        assertEquals(999, priority);
        assertTrue(appliesTo);
        assertEquals(Money.moneyEUR(100.0), refundAmount);
    }

}
