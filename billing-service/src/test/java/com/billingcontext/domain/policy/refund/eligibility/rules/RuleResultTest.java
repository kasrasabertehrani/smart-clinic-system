package com.billingcontext.domain.policy.refund.eligibility.rules;

import com.billingcontext.domain.policy.Evaluation;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RuleResultTest {
    @Test
    void testRuleResultCreationWhenStatusIsApproved(){
        Evaluation status = Evaluation.APPROVE;
        RuleResult ruleResult = new RuleResult(status, null);

        assertEquals(status, ruleResult.status());
        assertNull(ruleResult.reason());
    }
    @Test
    void testRuleResultCreationWhenStatusIsDenied(){
        Evaluation status =  Evaluation.DENY;
        RuleResult ruleResult = new RuleResult(status, "Test");

        assertEquals(status, ruleResult.status());
        assertEquals("Test", ruleResult.reason());
    }
    @Test
    void testRuleResultCreationWhenStatusIsNeutral(){
        Evaluation status =  Evaluation.NEUTRAL;
        RuleResult ruleResult = new RuleResult(status, null);

        assertEquals(status, ruleResult.status());
        assertNull(ruleResult.reason());
    }
    @Test
    void testRuleResultWithInvalidData(){
        assertThrows(RuleResultException.class, () -> new RuleResult(null, null));
        assertThrows(RuleResultException.class, () -> new RuleResult(Evaluation.DENY, null));
        assertThrows(RuleResultException.class, () -> new RuleResult(Evaluation.DENY, ""));
        assertThrows(RuleResultException.class, () -> new RuleResult(Evaluation.APPROVE, "Test"));
        assertThrows(RuleResultException.class, () -> new RuleResult(Evaluation.NEUTRAL, "Test"));
    }
    @Test
    void testRuleResultFactoryMethods(){
        RuleResult forceApprove = RuleResult.forceApprove();
        assertEquals(Evaluation.APPROVE, forceApprove.status());
        assertNull(forceApprove.reason());

        RuleResult deny = RuleResult.deny("Test");
        assertEquals(Evaluation.DENY, deny.status());
        assertEquals("Test", deny.reason());

        RuleResult neutral = RuleResult.neutral();
        assertEquals(Evaluation.NEUTRAL, neutral.status());
        assertNull(neutral.reason());
    }
}
