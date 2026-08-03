package com.billingcontext.domain.policy.pricing.evaluation;

import com.billingcontext.domain.TestFixtures;
import com.billingcontext.domain.shared.finance.Money;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PricingEvaluationContextTest {
    @Test
    void testPricingEvaluationContextCreation(){
        Money basePrice = Money.moneyEUR(100.00);
        LocalDateTime paymentDueDate = TestFixtures.localDateTimeOf(10, 25);
        LocalDateTime proformaCreationDate = TestFixtures.localDateTimeOf(5, 20);
        Clock clock = Clock.systemDefaultZone();

        PricingEvaluationContext pricingEvaluationContext = new PricingEvaluationContext(
                basePrice, proformaCreationDate, paymentDueDate,  clock);

        assertEquals(basePrice, pricingEvaluationContext.basePrice());
        assertEquals(paymentDueDate, pricingEvaluationContext.paymentDueDate());
        assertEquals(proformaCreationDate, pricingEvaluationContext.proformaCreationDate());
        assertEquals(clock, pricingEvaluationContext.clock());
    }
    @Test
    void testPricingEvaluationContextCreationWithZeroBasePrice(){
        Money basePrice = Money.moneyEUR(0.00);
        LocalDateTime paymentDueDate = TestFixtures.localDateTimeOf(10, 25);
        LocalDateTime proformaCreationDate = TestFixtures.localDateTimeOf(5, 20);
        Clock clock = Clock.systemDefaultZone();

        assertThrows(PricingEvaluationContextException.class, () -> {
            new PricingEvaluationContext(basePrice, proformaCreationDate, paymentDueDate, clock);
        });
    }
    @Test
    void testPricingEvaluationContextCreationWithProformaCreationDateAfterPaymentDueDate(){
        Money basePrice = Money.moneyEUR(100.00);
        LocalDateTime paymentDueDate = TestFixtures.localDateTimeOf(5, 20);
        LocalDateTime proformaCreationDate = TestFixtures.localDateTimeOf(12, 25);
        Clock clock = Clock.systemDefaultZone();

        assertThrows(PricingEvaluationContextException.class, () -> {
            new PricingEvaluationContext(basePrice, proformaCreationDate, paymentDueDate, clock);
        });
    }
    @Test
    void testPricingEvaluationContextCreationWithProformaCreationDateAfterNow(){
        Money basePrice = Money.moneyEUR(100.00);
        LocalDateTime paymentDueDate = TestFixtures.localDateTimeOf(10, 25);
        LocalDateTime proformaCreationDate = TestFixtures.localDateTimeOf(8, 25);
        Clock clock = Clock.systemDefaultZone();

        assertThrows(PricingEvaluationContextException.class, () -> {
            new PricingEvaluationContext(basePrice, proformaCreationDate, paymentDueDate, clock);
        });
    }
    @Test
    void testPricingEvaluationContextCreationWithPaymentDueDateBeforeNow(){
        Money basePrice = Money.moneyEUR(100.00);
        LocalDateTime paymentDueDate = TestFixtures.localDateTimeOf(5, 20);
        LocalDateTime proformaCreationDate = TestFixtures.localDateTimeOf(4, 25);
        Clock clock = Clock.systemDefaultZone();

        assertThrows(PricingEvaluationContextException.class, () -> {
            new PricingEvaluationContext(basePrice, proformaCreationDate, paymentDueDate, clock);
        });
    }
    @Test
    void testMinutesAfterProformaCreation() {
        Instant now = Instant.parse("2026-07-04T12:00:00Z");
        Clock clock = Clock.fixed(now, ZoneOffset.UTC);

        LocalDateTime creation = LocalDateTime.now(clock).minusMinutes(30);
        LocalDateTime due = LocalDateTime.now(clock).plusMinutes(25);

        PricingEvaluationContext context =
                new PricingEvaluationContext(
                        Money.moneyEUR(100),
                        creation,
                        due,
                        clock
                );

        assertEquals(30, context.minutesAfterProformaCreation());
    }
    @Test
    void testMinutesUntilPaymentDueDate() {
        Instant now = Instant.parse("2026-07-04T12:00:00Z");
        Clock clock = Clock.fixed(now, ZoneOffset.UTC);
        LocalDateTime creation = LocalDateTime.now(clock).minusMinutes(30);
        LocalDateTime due = LocalDateTime.now(clock).plusMinutes(25);

        PricingEvaluationContext context =
                new PricingEvaluationContext(
                        Money.moneyEUR(100),
                        creation,
                        due,
                        clock
                );
        assertEquals(25, context.minutesToPaymentDueDate());
    }
}
