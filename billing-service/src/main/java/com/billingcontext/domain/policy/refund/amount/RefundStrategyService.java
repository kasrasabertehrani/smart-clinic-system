package com.billingcontext.domain.policy.refund.amount;

import com.billingcontext.domain.policy.refund.amount.strategy.RefundCalculationStrategy;
import com.billingcontext.domain.policy.refund.evaluation.RefundEvaluationContext;
import com.billingcontext.domain.policy.refund.amount.strategy.DefaultStrategy;
import lombok.Getter;

import java.util.Comparator;
import java.util.List;

@Getter
public class RefundStrategyService {
    private final List<RefundCalculationStrategy> sortedConditionalStrategies;
    private final RefundCalculationStrategy defaultStrategy;

    public RefundStrategyService(List<RefundCalculationStrategy> conditionalStrategies) {

        this.sortedConditionalStrategies = conditionalStrategies.stream()
                .sorted(Comparator.comparingInt(RefundCalculationStrategy::priority))
                .toList();

        this.defaultStrategy = new DefaultStrategy();
    }

    public RefundCalculationStrategy determineStrategy(RefundEvaluationContext context) {

        return sortedConditionalStrategies.stream()
                .filter(strategy -> strategy.appliesTo(context))
                .findFirst()
                .orElse(defaultStrategy);
    }
}
