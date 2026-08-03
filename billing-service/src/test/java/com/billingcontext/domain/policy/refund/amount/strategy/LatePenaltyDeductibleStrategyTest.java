package com.billingcontext.domain.policy.refund.amount.strategy;

import com.billingcontext.domain.TestFixtures;
import com.billingcontext.domain.policy.refund.evaluation.RefundEvaluationContext;
import com.billingcontext.domain.shared.finance.Money;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LatePenaltyDeductibleStrategyTest {
    @Test
    void testLatePenaltyDeductibleStrategyWhenPatientCanceledTheAppointmentWithin48Hours() {
        RefundEvaluationContext refundEvaluationContext = TestFixtures.createRefundEvaluationContext(Money.moneyEUR(100.0),
                "patient", 100L,2500L);
        RefundCalculationStrategy refundCalculationStrategy = new LatePenaltyDeductibleStrategy();
        int priority = refundCalculationStrategy.priority();
        boolean appliesTo = refundCalculationStrategy.appliesTo(refundEvaluationContext);
        Money refundAmount = refundCalculationStrategy.calculateRefundAmount(refundEvaluationContext);

        assertEquals(2, priority);
        assertTrue(appliesTo);
        assertEquals(Money.moneyEUR(80.0), refundAmount);
    }
    @Test
    void testLatePenaltyDeductibleStrategyWhenPatientCanceledTheAppointmentMoreThan48Hours() {
        RefundEvaluationContext refundEvaluationContext = TestFixtures.createRefundEvaluationContext(Money.moneyEUR(100.0),
                "patient", 100L,3500L);
        RefundCalculationStrategy refundCalculationStrategy = new LatePenaltyDeductibleStrategy();
        int priority = refundCalculationStrategy.priority();
        boolean appliesTo = refundCalculationStrategy.appliesTo(refundEvaluationContext);
        Money refundAmount = refundCalculationStrategy.calculateRefundAmount(refundEvaluationContext);

        assertEquals(2, priority);
        assertFalse(appliesTo);
        assertEquals(Money.moneyEUR(80.0), refundAmount);
    }
    @Test
    void testLatePenaltyDeductibleStrategyWhenAnotherInitiatorCanceledTheAppointment() {
        RefundEvaluationContext refundEvaluationContext = TestFixtures.createRefundEvaluationContext(Money.moneyEUR(100.0),
                "system", 100L,3500L);
        RefundCalculationStrategy refundCalculationStrategy = new LatePenaltyDeductibleStrategy();
        int priority = refundCalculationStrategy.priority();
        boolean appliesTo = refundCalculationStrategy.appliesTo(refundEvaluationContext);
        Money refundAmount = refundCalculationStrategy.calculateRefundAmount(refundEvaluationContext);

        assertEquals(2, priority);
        assertFalse(appliesTo);
        assertEquals(Money.moneyEUR(80.0), refundAmount);
    }

}
