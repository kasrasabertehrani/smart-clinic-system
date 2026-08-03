package com.billingcontext.domain.policy.refund.amount.strategy;

import com.billingcontext.domain.shared.finance.Money;
import com.billingcontext.domain.policy.refund.evaluation.RefundEvaluationContext;

public class DefaultStrategy implements RefundCalculationStrategy {
    @Override
    public int priority() { return 999; }

    @Override
    public boolean appliesTo(RefundEvaluationContext context) {
        return true;
    }

    @Override
    public Money calculateRefundAmount(RefundEvaluationContext context) {
        return context.totalAmount();
    }
}
