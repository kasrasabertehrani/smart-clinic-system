package com.billingcontext.domain.policy.refund.eligibility;

import com.billingcontext.domain.TestFixtures;
import com.billingcontext.domain.policy.refund.evaluation.RefundEvaluationContext;
import com.billingcontext.domain.policy.refund.eligibility.rules.BookingGracePeriodSpecification;
import com.billingcontext.domain.policy.refund.eligibility.rules.ClinicInitiatedSpecification;
import com.billingcontext.domain.policy.refund.eligibility.rules.MinimumNoticeSpecification;
import java.util.Optional;

import com.billingcontext.domain.policy.refund.eligibility.rules.RefundEligibilityRule;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RefundEligibilityServiceTest {
    @Test
    void testRefundEligibilityPolicyCreation(){
        List<RefundEligibilityRule> refundEligibilitySpecifications =
                TestFixtures.createRefundEligibilitySpecifications();

        RefundEligibilityService allSpecifications = new RefundEligibilityService(refundEligibilitySpecifications);

        assertEquals(2, allSpecifications.getOverrideSpecs().size());
        assertEquals(1, allSpecifications.getStandardSpecs().size());
        assertEquals(0, allSpecifications.getVetoSpecs().size());
        assertInstanceOf(BookingGracePeriodSpecification.class, allSpecifications.getOverrideSpecs().get(1));
        assertInstanceOf(ClinicInitiatedSpecification.class, allSpecifications.getOverrideSpecs().get(0));
        assertInstanceOf(MinimumNoticeSpecification.class, allSpecifications.getStandardSpecs().get(0));
    }
    @Test
    void testRefundEligibilityPolicyCreationWithEmptyListOfRules(){
        List<RefundEligibilityRule> refundEligibilitySpecifications = List.of();

        assertThrows(RefundEligibilityServiceException.class, () ->
                new RefundEligibilityService(refundEligibilitySpecifications));
    }
    @Test
    void testRefundEligibilityPolicyEvaluationAndPatientIsEligibleForRefundWhenClinicCanceled(){
        RefundEvaluationContext refundEvaluationContext = TestFixtures.createRefundEvaluationContext(
                "system", 10L, 5000L);
        List<RefundEligibilityRule> refundEligibilitySpecifications =
                TestFixtures.createRefundEligibilitySpecifications();

        RefundEligibilityService allSpecifications = new RefundEligibilityService(refundEligibilitySpecifications);

        Optional<String> eligibility = allSpecifications.evaluate(refundEvaluationContext);

        assertFalse(eligibility.isPresent());
    }
    @Test
    void testRefundEligibilityPolicyEvaluationAndPatientIsEligibleForRefundWhenPatientCanceledDuringGracePeriod(){
        RefundEvaluationContext refundEvaluationContext = TestFixtures.createRefundEvaluationContext(
                "patient", 10L, 5000L);
        List<RefundEligibilityRule> refundEligibilitySpecifications =
                TestFixtures.createRefundEligibilitySpecifications();

        RefundEligibilityService allSpecifications = new RefundEligibilityService(refundEligibilitySpecifications);

        Optional<String> eligibility = allSpecifications.evaluate(refundEvaluationContext);

        assertFalse(eligibility.isPresent());
    }
    @Test
    void testRefundEligibilityPolicyEvaluationAndPatientIsNotEligibleForRefundWhenMinimumNoticePeriodHasPassed(){
        RefundEvaluationContext refundEvaluationContext = TestFixtures.createRefundEvaluationContext(
                "patient", 20L, 1000L);
        List<RefundEligibilityRule> refundEligibilitySpecifications =
                TestFixtures.createRefundEligibilitySpecifications();

        RefundEligibilityService allSpecifications = new RefundEligibilityService(refundEligibilitySpecifications);

        Optional<String> eligibility = allSpecifications.evaluate(refundEvaluationContext);

        assertTrue(eligibility.isPresent());
    }
    @Test
    void testRefundEligibilityPolicyEvaluationAndPatientIsEligibleForRefundWhenMinimumNoticePeriodHasNotPassed(){
        RefundEvaluationContext refundEvaluationContext = TestFixtures.createRefundEvaluationContext(
                "patient", 30L, 2000L);
        List<RefundEligibilityRule> refundEligibilitySpecifications =
                TestFixtures.createRefundEligibilitySpecifications();

        RefundEligibilityService allSpecifications = new RefundEligibilityService(refundEligibilitySpecifications);

        Optional<String> eligibility = allSpecifications.evaluate(refundEvaluationContext);

        assertFalse(eligibility.isPresent());
    }
}
