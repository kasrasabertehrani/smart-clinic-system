package com.billingcontext.domain.policy.pricing;

import com.billingcontext.domain.shared.finance.Money;
import com.billingcontext.domain.shared.finance.Percentage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PricingPolicyResultTest {
    @Test
    void testPricingPolicyResultCreation() {
        PricingPolicyResult result = new PricingPolicyResult(
                Money.moneyEUR(100.00),
                Percentage.of(10.0),
                Percentage.of(10.0),
                Money.moneyEUR(99.00)
        );

        assertNotNull(result);
        assertEquals(Money.moneyEUR(100.00), result.basePrice());
        assertEquals(Percentage.of(10.0), result.discountPercentage());
        assertEquals(Percentage.of(10.0), result.taxPercentage());
        assertEquals(Money.moneyEUR(99.00), result.finalPrice());
    }
    @Test
    void testPricingPolicyResultCreationWithInvalidData() {

        assertThrows(PricingPolicyResultException.class, () -> {
            new PricingPolicyResult(
                    Money.moneyEUR(0.00),
                    Percentage.of(10.0),
                    Percentage.of(10.0),
                    Money.moneyEUR(99.00)
            );
        });

        assertThrows(PricingPolicyResultException.class, () -> {
            new PricingPolicyResult(
                    Money.moneyEUR(100.0),
                    Percentage.of(0.0),
                    Percentage.of(10.0),
                    Money.moneyEUR(99.0)
            );
        });
        assertThrows(PricingPolicyResultException.class, () -> {
            new PricingPolicyResult(
                    Money.moneyEUR(100.0),
                    Percentage.of(100.0),
                    Percentage.of(10.0),
                    Money.moneyEUR(10.0)
            );
        });

    }
    @Test
    void testPricingPolicyResultCreationWithOneHundredDiscount() {
        PricingPolicyResult result = new PricingPolicyResult(
                Money.moneyEUR(100.0),
                Percentage.of(100.0),
                Percentage.of(10.0),
                Money.moneyEUR(0.0)
        );
        assertNotNull(result);
        assertEquals(Money.moneyEUR(100.00), result.basePrice());
        assertEquals(Percentage.of(100.0), result.discountPercentage());
        assertEquals(Percentage.of(10.0), result.taxPercentage());
        assertEquals(Money.moneyEUR(0.0), result.finalPrice());
    }
    @Test
    void testPricingPolicyResultCreationWithZeroDiscount() {
        PricingPolicyResult result = new PricingPolicyResult(
                Money.moneyEUR(100.0),
                Percentage.of(0.0),
                Percentage.of(10.0),
                Money.moneyEUR(110.0)
        );
        assertNotNull(result);
        assertEquals(Money.moneyEUR(100.00), result.basePrice());
        assertEquals(Percentage.of(0.0), result.discountPercentage());
        assertEquals(Percentage.of(10.0), result.taxPercentage());
        assertEquals(Money.moneyEUR(110.0), result.finalPrice());
    }
    @Test
    void testIsAppointmentFree(){
        PricingPolicyResult result = new PricingPolicyResult(
                Money.moneyEUR(100.0),
                Percentage.of(100.0),
                Percentage.of(10.0),
                Money.moneyEUR(0.0)
        );
        assertTrue(result.isFree());
    }
    @Test
    void testIsAppointmentIsNotFree(){
        PricingPolicyResult result = new PricingPolicyResult(
                Money.moneyEUR(100.0),
                Percentage.of(10.0),
                Percentage.of(10.0),
                Money.moneyEUR(110.0)
        );
        assertFalse(result.isFree());
    }

}
