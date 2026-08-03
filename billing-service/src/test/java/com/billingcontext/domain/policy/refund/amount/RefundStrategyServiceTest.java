package com.billingcontext.domain.policy.refund.amount;

import com.billingcontext.domain.TestFixtures;
import com.billingcontext.domain.policy.refund.amount.strategy.DefaultStrategy;
import com.billingcontext.domain.policy.refund.amount.strategy.FullRefundStrategy;
import com.billingcontext.domain.policy.refund.amount.strategy.LatePenaltyDeductibleStrategy;
import com.billingcontext.domain.policy.refund.amount.strategy.RefundCalculationStrategy;
import com.billingcontext.domain.policy.refund.evaluation.RefundEvaluationContext;
import com.billingcontext.domain.shared.finance.Money;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

public class RefundStrategyServiceTest {
    @Test
    void testRefundStrategyServiceCreation(){
        List<RefundCalculationStrategy> refundCalculationStrategies = TestFixtures.createRefundCalculationStrategies();

        RefundStrategyService refundStrategyService = new RefundStrategyService(refundCalculationStrategies);

        assertEquals(2, refundStrategyService.getSortedConditionalStrategies().size());
        assertInstanceOf(LatePenaltyDeductibleStrategy.class, refundStrategyService.getSortedConditionalStrategies().get(0));
        assertInstanceOf(FullRefundStrategy.class, refundStrategyService.getSortedConditionalStrategies().get(1));
    }
    @Test
    void testStrategySelectionWhenLatePenaltyDeductibleApplies(){
        List<RefundCalculationStrategy> refundCalculationStrategies = TestFixtures.createRefundCalculationStrategies();
        RefundEvaluationContext refundEvaluationContext = TestFixtures.createRefundEvaluationContext(
                Money.moneyEUR(100.0), "patient", 100L,1000L);
        RefundStrategyService refundStrategyService = new RefundStrategyService(refundCalculationStrategies);

        RefundCalculationStrategy finalStrategy = refundStrategyService.determineStrategy(refundEvaluationContext);

        assertInstanceOf(LatePenaltyDeductibleStrategy.class, finalStrategy);
    }
    @Test
    void testStrategySelectionWhenFullRefundApplies(){
        List<RefundCalculationStrategy> refundCalculationStrategies = TestFixtures.createRefundCalculationStrategies();
        RefundEvaluationContext refundEvaluationContext = TestFixtures.createRefundEvaluationContext(
                Money.moneyEUR(100.0), "system", 100L,1000L);
        RefundStrategyService refundStrategyService = new RefundStrategyService(refundCalculationStrategies);

        RefundCalculationStrategy finalStrategy = refundStrategyService.determineStrategy(refundEvaluationContext);

        assertInstanceOf(FullRefundStrategy.class, finalStrategy);
    }
    @Test
    void testStrategySelectionWhenDefaultStrategyApplies(){
        List<RefundCalculationStrategy> refundCalculationStrategies = TestFixtures.createRefundCalculationStrategies();
        RefundEvaluationContext refundEvaluationContext = TestFixtures.createRefundEvaluationContext(
                Money.moneyEUR(100.0), "patient", 100L,3000L);
        RefundStrategyService refundStrategyService = new RefundStrategyService(refundCalculationStrategies);

        RefundCalculationStrategy finalStrategy = refundStrategyService.determineStrategy(refundEvaluationContext);

        assertInstanceOf(DefaultStrategy.class, finalStrategy);
    }


}
