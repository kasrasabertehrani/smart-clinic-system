package com.billingcontext.domain.policy.refund.amount.strategy;



import com.billingcontext.domain.shared.finance.Money;
import com.billingcontext.domain.policy.refund.evaluation.RefundEvaluationContext;

public class FullRefundStrategy implements RefundCalculationStrategy {

    @Override
    public int priority() {
        return 1;
    }

    @Override
    public boolean appliesTo(RefundEvaluationContext context) {
        if(context.isCanceledByPatient()){
            return context.durationSinceBooking() < 15L;
        }
        return context.isCanceledByClinicStaff() || context.isCanceledBySystemAutomation();
    }

    @Override
    public Money calculateRefundAmount(RefundEvaluationContext context) {
        return context.totalAmount();
    }
}
