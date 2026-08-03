package com.billingcontext.domain.policy.refund;

import com.billingcontext.domain.TestFixtures;
import com.billingcontext.domain.policy.refund.evaluation.RefundEvaluationContext;
import com.billingcontext.domain.shared.finance.Money;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RefundPolicyServiceTest {
    @Test
    void testRefundPolicyServiceWhenPatientIsEligibleForRefund() {
        RefundPolicyService refundPolicyService = TestFixtures.createRefundPolicyService();
        RefundEvaluationContext refundEvaluationContext = TestFixtures.createRefundEvaluationContext(
                Money.moneyEUR(100.00), "patient", 100L, 2000L);

        RefundPolicyResult result = refundPolicyService.processRefundPolicies(refundEvaluationContext);

        assertEquals(RefundPolicyResult.approved(Money.moneyEUR(80.00)), result);
    }
    @Test
    void testRefundPolicyServiceWhenPatientIsNotEligibleForRefund() {
        RefundPolicyService refundPolicyService = TestFixtures.createRefundPolicyService();
        RefundEvaluationContext refundEvaluationContext = TestFixtures.createRefundEvaluationContext(
                Money.moneyEUR(100.00), "patient", 20L, 500L);

        RefundPolicyResult result = refundPolicyService.processRefundPolicies(refundEvaluationContext);

        assertEquals(RefundPolicyResult.denied("Patient canceled with less than 24 hours notice.",
                refundEvaluationContext.totalAmount().currency()), result);
    }


}
