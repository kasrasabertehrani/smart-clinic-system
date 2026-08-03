package com.billingcontext.domain.policy.refund;

import com.billingcontext.domain.policy.refund.amount.RefundStrategyService;
import com.billingcontext.domain.policy.refund.amount.strategy.RefundCalculationStrategy;
import com.billingcontext.domain.policy.refund.eligibility.RefundEligibilityService;
import com.billingcontext.domain.policy.refund.evaluation.RefundEvaluationContext;
import com.billingcontext.domain.shared.finance.Money;

import java.util.Optional;

public class RefundPolicyService {

    private final RefundStrategyService refundStrategyService;
    private final RefundEligibilityService refundEligibilityService;

    public RefundPolicyService(RefundStrategyService refundStrategyService, RefundEligibilityService refundEligibilityService) {
        this.refundStrategyService = refundStrategyService;
        this.refundEligibilityService = refundEligibilityService;
    }

    public RefundPolicyResult processRefundPolicies(RefundEvaluationContext context){
        Optional<String> denialReason = refundEligibilityService.evaluate(context);
        if (denialReason.isPresent()) {
            return RefundPolicyResult.denied(denialReason.get(), context.totalAmount().currency());
        }
        RefundCalculationStrategy refundCalculationStrategy = refundStrategyService.determineStrategy(context);
        Money refundAmount = refundCalculationStrategy.calculateRefundAmount(context);
        return RefundPolicyResult.approved(refundAmount);
    }

}
