package com.billingcontext.domain.policy.refund.amount.strategy;

import com.billingcontext.domain.TestFixtures;
import com.billingcontext.domain.policy.refund.evaluation.RefundEvaluationContext;
import com.billingcontext.domain.shared.finance.Money;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class FullRefundStrategyTest {
    @Test
    void testFullRefundStrategyWhenCancelInitiatorIsPatient() {
        RefundEvaluationContext refundEvaluationContext = TestFixtures.createRefundEvaluationContext(Money.moneyEUR(100.0),
                "patient", 100L,1000L);
        RefundCalculationStrategy refundCalculationStrategy = new FullRefundStrategy();
        int priority = refundCalculationStrategy.priority();
        boolean appliesTo = refundCalculationStrategy.appliesTo(refundEvaluationContext);
        Money refundAmount = refundCalculationStrategy.calculateRefundAmount(refundEvaluationContext);

        assertEquals(10, priority);
        assertFalse(appliesTo);
        assertEquals(Money.moneyEUR(100.0), refundAmount);
    }
    @Test
    void testFullRefundStrategyWhenCancelInitiatorIsClinic() {
        RefundEvaluationContext refundEvaluationContext = TestFixtures.createRefundEvaluationContext(Money.moneyEUR(100.0),
                "clinic", 100L,1000L);
        RefundCalculationStrategy refundCalculationStrategy = new FullRefundStrategy();
        int priority = refundCalculationStrategy.priority();
        boolean appliesTo = refundCalculationStrategy.appliesTo(refundEvaluationContext);
        Money refundAmount = refundCalculationStrategy.calculateRefundAmount(refundEvaluationContext);

        assertEquals(10, priority);
        assertTrue(appliesTo);
        assertEquals(Money.moneyEUR(100.0), refundAmount);
    }
    @Test
    void testFullRefundStrategyWhenCancelInitiatorIsSystem() {
        RefundEvaluationContext refundEvaluationContext = TestFixtures.createRefundEvaluationContext(Money.moneyEUR(100.0),
                "system", 100L,1000L);
        RefundCalculationStrategy refundCalculationStrategy = new FullRefundStrategy();
        int priority = refundCalculationStrategy.priority();
        boolean appliesTo = refundCalculationStrategy.appliesTo(refundEvaluationContext);
        Money refundAmount = refundCalculationStrategy.calculateRefundAmount(refundEvaluationContext);

        assertEquals(10, priority);
        assertTrue(appliesTo);
        assertEquals(Money.moneyEUR(100.0), refundAmount);
    }
}
