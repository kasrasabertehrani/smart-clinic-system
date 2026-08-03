package com.billingcontext.domain.policy.refund.amount.strategy;



import com.billingcontext.domain.shared.finance.Money;
import com.billingcontext.domain.policy.refund.evaluation.RefundEvaluationContext;

public class LatePenaltyDeductibleStrategy implements RefundCalculationStrategy {

    public static final double LATE_PENALTY_FEE = 20.0;

    @Override
    public int priority() { return 10; }

    @Override
    public boolean appliesTo(RefundEvaluationContext context) {
        return context.isCanceledByPatient() && context.minutesUntilAppointment() < (48 * 60);
    }

    @Override
    public Money calculateRefundAmount(RefundEvaluationContext context) {
        String currency = context.totalAmount().currency().toString();
        Money penaltyAmount = Money.of(LATE_PENALTY_FEE, currency);
        return context.totalAmount().subtract(penaltyAmount);
    }
}
