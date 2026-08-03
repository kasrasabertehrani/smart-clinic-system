package com.billingcontext.domain.policy.refund.eligibility.rules;

import com.billingcontext.domain.TestFixtures;
import com.billingcontext.domain.policy.refund.evaluation.RefundEvaluationContext;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MinimumNoticeSpecificationTest {
    @Test
    void testEvaluateMinimumNoticeSpecificationWhenPatientCanceledAppointmentMoreThanMinimumNotice() {
        RefundEvaluationContext refundEvaluationContext = TestFixtures.createRefundEvaluationContext(
                "patient", 10L, 1500L);
        RefundEligibilityRule specification = new MinimumNoticeSpecification();

        RuleResult result = specification.evaluate(refundEvaluationContext);
        assertEquals(RuleResult.neutral(), result);
    }
    @Test
    void testEvaluateMinimumNoticeSpecificationWhenPatientCanceledAppointmentLessThanMinimumNotice() {
        RefundEvaluationContext refundEvaluationContext = TestFixtures.createRefundEvaluationContext(
                "patient", 10L, 1000L);
        RefundEligibilityRule specification = new MinimumNoticeSpecification();

        RuleResult result = specification.evaluate(refundEvaluationContext);
        assertEquals(RuleResult.deny("Patient canceled with less than 24 hours notice."), result);
    }
    @Test
    void testEvaluateMinimumNoticeSpecificationWhenCancelInitiatorIsNotPatient() {
        RefundEvaluationContext refundEvaluationContext = TestFixtures.createRefundEvaluationContext(
                "system", 10L, 1500L);
        RefundEligibilityRule specification = new MinimumNoticeSpecification();

        RuleResult result = specification.evaluate(refundEvaluationContext);
        assertEquals(RuleResult.neutral(), result);
    }
    @Test
    void testEvaluateRuleOrder(){
        RefundEligibilityRule specification = new MinimumNoticeSpecification();

        RuleOrder order  = specification.order();

        assertEquals(RuleOrder.RuleTier.STANDARD, order.tier());
        assertEquals(1, order.priority());
    }
}
