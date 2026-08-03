package com.billingcontext.domain.policy.refund.amount.strategy;

import com.billingcontext.domain.shared.finance.Money;
import com.billingcontext.domain.policy.refund.evaluation.RefundEvaluationContext;

public interface RefundCalculationStrategy {
    Money calculateRefundAmount(RefundEvaluationContext context);
    int priority();
    boolean appliesTo(RefundEvaluationContext context);
}
