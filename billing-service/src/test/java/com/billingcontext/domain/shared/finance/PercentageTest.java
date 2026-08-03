package com.billingcontext.domain.shared.finance;

import com.billingcontext.domain.TestFixtures;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class PercentageTest {
    @Test
    void testValidPercentageCreation(){
        Percentage canRound = TestFixtures.percentageOf("25.236");
        assertEquals(new BigDecimal("25.24"), canRound.value());

        Percentage canNotRound = TestFixtures.percentageOf("25.245");
        assertEquals(new BigDecimal("25.24"), canNotRound.value());

        Percentage canRoundHalfEven = TestFixtures.percentageOf("25.275");
        assertEquals(new BigDecimal("25.28"), canRoundHalfEven.value());
    }
    @Test
    void testInvalidPercentageCreation(){
        assertThrows(PercentageException.class, () -> {
            TestFixtures.percentageOf("-25.00");
        });
        assertThrows(PercentageException.class, () -> {
            TestFixtures.percentageOf("125.00");
        });
    }
    @Test
    void testPercentageOf(){
        Percentage percentage = Percentage.of(25.236);
        assertEquals(new BigDecimal("25.24"), percentage.value());
    }
    @Test
    void testPercentageZero(){
        Percentage percentage = Percentage.zero();
        assertEquals(new BigDecimal("0.00"), percentage.value());
    }
    @Test
    void testAddPercentage(){
        Percentage percentage = TestFixtures.percentageOf("25.00");
        Percentage percentage2 = TestFixtures.percentageOf("65.00");
        Percentage percentage3 = TestFixtures.percentageOf("95.00");

        Percentage lessThanHundred = percentage.add(percentage2);
        Percentage greaterThanHundred = percentage.add(percentage3);

        assertEquals(new BigDecimal("90.00"), lessThanHundred.value());
        assertEquals(new BigDecimal("100.00"), greaterThanHundred.value());
    }
    @Test
    void testIsGreaterThan(){
        Percentage percentage = TestFixtures.percentageOf("25.00");
        Percentage percentage2 = TestFixtures.percentageOf("65.00");

        assertFalse(percentage.isGreaterThan(percentage2));
        assertTrue(percentage2.isGreaterThan(percentage));
    }
    @Test
    void testApplyTo(){
        Percentage percentage = TestFixtures.percentageOf("25.00");
        Money money = TestFixtures.moneyOf("100.00", "USD");

        Money discountedPrice = percentage.applyDiscountTo(money);

        assertEquals(TestFixtures.moneyOf("75.00", "USD"), discountedPrice);
    }
}
