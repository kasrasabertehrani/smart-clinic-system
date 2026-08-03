package com.billingcontext.domain.shared.finance;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;


import static com.billingcontext.domain.TestFixtures.*;
import static org.junit.jupiter.api.Assertions.*;


public class MoneyTest {

    @Test
    void testMoneyCreation() {
       Money money = moneyOf("1.3456", "USD");

       assertEquals(money.amount(), bigDecimalOf("1.3456"));
       assertEquals(money.currency(), currencyOf("USD"));
    }
    @Test
    void testMoneyCreationWithNegativeAmount() {
        assertThrows(MoneyException.class, () -> moneyOf("-1.3456", "USD"));
    }
    @Test
    void testMoneyCreationWithNullCurrency() {
        BigDecimal amount = new BigDecimal("1.3456");

        assertThrows(MoneyException.class, () -> new Money(amount, null));
    }
    @Test
    void testMoneyAmountDecimalPoints() {
        Money money = moneyOf("1.34", "USD");

        assertEquals(4, money.amount().scale());
        assertEquals(bigDecimalOf("1.3400"), money.amount());
    }
    @Test
    void testMoneyAdditionWithSameCurrency() {
        Money money1 = moneyEUR("1.50");
        Money money2 = moneyEUR("2.50");

        Money moneySum = money1.add(money2);
        assertEquals(bigDecimalOf("4.00"), moneySum.amount());
    }
    @Test
    void testMoneyAdditionWithDifferentCurrency() {
        Money money1 = moneyUSD("1.50");
        Money money2 = moneyEUR("2.50");

        assertThrows(MoneyException.class, () -> money1.add(money2));
    }
    @Test
    void testMoneySubtractionWithSameCurrency() {
        Money money1 = moneyEUR("1.50");
        Money money2 = moneyEUR("2.50");

        Money moneySub = money2.subtract(money1);
        assertEquals(bigDecimalOf("1.00"), moneySub.amount());
    }
    @Test
    void testMoneySubtractionWithDifferentCurrency() {
        Money money1 = moneyEUR("1.50");
        Money money2 = moneyUSD("2.50");

        assertThrows(MoneyException.class, () -> money2.subtract(money1));
    }
    @Test
    void testMoneySubtractionWhenOneIsSmallerThanTheOther(){
        Money money1 = moneyEUR("1.50");
        Money money2 = moneyEUR("2.50");

        assertThrows(MoneyException.class, () -> money1.subtract(money2));
    }
    @Test
    void testCurrencyConversion() {
        Money moneyEUR = moneyEUR("1.50");
        Money money = moneyEUR.convertTo(currencyOf("USD"), bigDecimalOf("1.10"));

        assertEquals(bigDecimalOf("1.65"), money.amount());
        assertEquals(currencyOf("USD"), money.currency());
    }
    @Test
    void testCurrencyConversionWithZeroOrLessConversionRate() {
        Money moneyEUR = moneyEUR("1.50");

        assertThrows(MoneyException.class, () -> moneyEUR.convertTo(currencyOf("USD"), bigDecimalOf("0")));
        assertThrows(MoneyException.class, () -> moneyEUR.convertTo(currencyOf("USD"), bigDecimalOf("-1.10")));
    }
    @Test
    void testFactoryMethodMoneyOf(){
        Money money = Money.of(1.3456, "USD");
        assertEquals(money.amount(), bigDecimalOf("1.3456"));
        assertEquals(money.currency(), currencyOf("USD"));
    }
    @Test
    void testFactoryMethodMoneyEUR(){
        Money money = Money.moneyEUR(1.3456);
        assertEquals(money.amount(), bigDecimalOf("1.3456"));
        assertEquals(money.currency(), currencyOf("EUR"));
    }
    @Test
    void testFactoryMethodMoneyUSD(){
        Money money = Money.moneyUSD(1.3456);
        assertEquals(money.amount(), bigDecimalOf("1.3456"));
        assertEquals(money.currency(), currencyOf("USD"));
    }
    @Test
    void testMoneyIsZero(){
        Money money = moneyEUR("0.0");
        assertTrue(money.isZero());
    }
    @Test
    void testMoneyIsNotZero(){
        Money money = moneyEUR("50.0");
        assertFalse(money.isZero());
    }
    @Test
    void testMultiplyMoneyByRate(){
        Money money = moneyEUR("1.50");
        BigDecimal rate = bigDecimalOf("2.00");

        Money moneyMultiply = money.multiply(rate);

        assertEquals(moneyEUR("3.00"), moneyMultiply);
    }
    @Test
    void testMultiplyMoneyByNegativeOrZeroRate(){
        Money money = moneyEUR("1.50");
        BigDecimal rateNegative = bigDecimalOf("-2.00");
        BigDecimal rateZero = bigDecimalOf("0.00");

        assertThrows(MoneyException.class, () -> money.multiply(rateNegative));
        assertThrows(MoneyException.class, () -> money.multiply(rateZero));
    }
    @Test
    void testMoneyZero(){
        Money money = Money.zero(currencyOf("USD"));
        assertTrue(money.isZero());
    }

}
