package com.billingcontext.domain.policy.refund.eligibility.rules;

import com.billingcontext.domain.TestFixtures;
import com.billingcontext.domain.policy.refund.evaluation.RefundEvaluationContext;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BookingGracePeriodSpecificationTest {

    @Test
    void testEvaluateGracePeriodWhenPatientCanceledAppointmentWithinGracePeriod() {
        RefundEvaluationContext refundEvaluationContext = TestFixtures.createRefundEvaluationContext(
                "patient", 10L, 5000L);
        RefundEligibilityRule specification = new BookingGracePeriodSpecification();

        RuleResult result = specification.evaluate(refundEvaluationContext);
        assertEquals(RuleResult.forceApprove(), result);
    }
    @Test
    void testEvaluateGracePeriodWhenPatientCanceledAppointmentAfterGracePeriod() {
        RefundEvaluationContext refundEvaluationContext = TestFixtures.createRefundEvaluationContext(
                "patient", 20L, 5000L
        );
        RefundEligibilityRule specification = new BookingGracePeriodSpecification();

        RuleResult result = specification.evaluate(refundEvaluationContext);

        assertEquals(RuleResult.neutral(), result);
    }
    @Test
    void testEvaluateGracePeriodWhenSystemCanceledAppointmentWithinGracePeriod() {
        RefundEvaluationContext refundEvaluationContext = TestFixtures.createRefundEvaluationContext(
                "clinic", 10L, 5000L
        );
        RefundEligibilityRule specification = new BookingGracePeriodSpecification();

        RuleResult result = specification.evaluate(refundEvaluationContext);

        assertEquals(RuleResult.neutral(), result);
    }
    @Test
    void testEvaluateRuleOrder(){
        RefundEligibilityRule specification = new BookingGracePeriodSpecification();
        RuleOrder order  = specification.order();

        assertEquals(RuleOrder.RuleTier.OVERRIDE, order.tier());
        assertEquals(2, order.priority());
    }
}
