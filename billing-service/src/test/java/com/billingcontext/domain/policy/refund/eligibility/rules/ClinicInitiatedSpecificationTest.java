package com.billingcontext.domain.policy.refund.eligibility.rules;

import com.billingcontext.domain.TestFixtures;
import com.billingcontext.domain.policy.refund.evaluation.RefundEvaluationContext;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ClinicInitiatedSpecificationTest {
    @Test
    void testClinicInitiatedSpecificationRuleWhenAppointmentWasCanceledBySystem(){
        RefundEvaluationContext refundEvaluationContext = TestFixtures.createRefundEvaluationContext(
                "system", 10L, 5000L);
        RefundEligibilityRule specification = new ClinicInitiatedSpecification();

        RuleResult result = specification.evaluate(refundEvaluationContext);
        assertEquals(RuleResult.forceApprove(), result);
    }
    @Test
    void testClinicInitiatedSpecificationRuleWhenAppointmentWasCanceledByClinicReception(){
        RefundEvaluationContext refundEvaluationContext = TestFixtures.createRefundEvaluationContext(
                "clinic", 10L, 5000L);
        RefundEligibilityRule specification = new ClinicInitiatedSpecification();

        RuleResult result = specification.evaluate(refundEvaluationContext);
        assertEquals(RuleResult.forceApprove(), result);
    }
    @Test
    void testClinicInitiatedSpecificationRuleWhenAppointmentWasCanceledByPatient(){
        RefundEvaluationContext refundEvaluationContext = TestFixtures.createRefundEvaluationContext(
                "patient", 10L, 5000L);
        RefundEligibilityRule specification = new ClinicInitiatedSpecification();

        RuleResult result = specification.evaluate(refundEvaluationContext);
        assertEquals(RuleResult.neutral(), result);
    }
    @Test
    void testEvaluateRuleOrder(){
        RefundEligibilityRule specification = new ClinicInitiatedSpecification();

        RuleOrder order  = specification.order();

        assertEquals(RuleOrder.RuleTier.OVERRIDE, order.tier());
        assertEquals(1, order.priority());
    }

}
