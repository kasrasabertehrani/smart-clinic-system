package com.billingcontext.domain.policy.refund.eligibility.rules;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RuleOrderTest {
    @Test
    void testRuleOrderCreation(){
        RuleOrder.RuleTier ruleTier = RuleOrder.RuleTier.OVERRIDE;
        int priority = 2;

        RuleOrder ruleOrder = new RuleOrder(ruleTier, priority);

        assertEquals(ruleTier, ruleOrder.tier());
        assertEquals(priority, ruleOrder.priority());
    }
    @Test
    void testRuleOrderCreationWithInvalidPriorityAndTier(){

        assertThrows(RuleOrderException.class, () -> {
            new RuleOrder(null, 2);
        });
        assertThrows(RuleOrderException.class, () -> {
            new RuleOrder(RuleOrder.RuleTier.STANDARD, 0);
        });
    }
    @Test
    void testRuleOrderCreationWithDifferentCategories(){
      RuleOrder veto =  RuleOrder.veto(1);
      RuleOrder override =  RuleOrder.override(2);
      RuleOrder standard =  RuleOrder.standard(3);

      assertEquals(RuleOrder.RuleTier.VETO, veto.tier());
      assertEquals(1, veto.priority());
      assertEquals(RuleOrder.RuleTier.STANDARD, standard.tier());
      assertEquals(3, standard.priority());
      assertEquals(RuleOrder.RuleTier.OVERRIDE, override.tier());
      assertEquals(2, override.priority());
    }
    @Test
    void testRuleOrderComparisonWhenRuleCategoryDoesNotMatch(){
        RuleOrder veto =  RuleOrder.veto(1);
        RuleOrder override =  RuleOrder.override(2);

        int result = veto.compareTo(override);
        assertEquals(-1, result);
    }
    @Test
    void testRuleOrderComparisonWhenRuleCategoryMatches(){
        RuleOrder veto =  RuleOrder.veto(1);
        RuleOrder anotherVeto =  RuleOrder.veto(2);

        int result = veto.compareTo(anotherVeto);
        assertEquals(-1, result);
    }
}
