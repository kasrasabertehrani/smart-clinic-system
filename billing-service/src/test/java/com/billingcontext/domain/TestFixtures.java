package com.billingcontext.domain;

import com.billingcontext.domain.invoice.AppointmentId;
import com.billingcontext.domain.invoice.AppointmentInvoice;
import com.billingcontext.domain.payment.*;
import com.billingcontext.domain.policy.pricing.PricingPolicyResult;
import com.billingcontext.domain.policy.pricing.evaluation.PricingEvaluationContext;
import com.billingcontext.domain.policy.pricing.discount.DiscountPolicyService;
import com.billingcontext.domain.policy.pricing.discount.rules.DiscountRule;
import com.billingcontext.domain.policy.pricing.discount.rules.EarlyClearanceDiscountRule;
import com.billingcontext.domain.policy.pricing.discount.rules.ImmediateSettlementDiscountRule;
import com.billingcontext.domain.policy.pricing.discount.rules.PromptPaymentDiscountRule;
import com.billingcontext.domain.policy.refund.RefundPolicyResult;
import com.billingcontext.domain.policy.refund.RefundPolicyService;
import com.billingcontext.domain.policy.refund.amount.RefundStrategyService;
import com.billingcontext.domain.policy.refund.amount.strategy.FullRefundStrategy;
import com.billingcontext.domain.policy.refund.amount.strategy.LatePenaltyDeductibleStrategy;
import com.billingcontext.domain.policy.refund.amount.strategy.RefundCalculationStrategy;
import com.billingcontext.domain.policy.refund.eligibility.RefundEligibilityService;
import com.billingcontext.domain.policy.refund.evaluation.CancellationInitiator;
import com.billingcontext.domain.policy.refund.evaluation.RefundEvaluationContext;
import com.billingcontext.domain.policy.refund.eligibility.rules.RefundEligibilityRule;
import com.billingcontext.domain.policy.refund.eligibility.rules.BookingGracePeriodSpecification;
import com.billingcontext.domain.policy.refund.eligibility.rules.ClinicInitiatedSpecification;
import com.billingcontext.domain.policy.refund.eligibility.rules.MinimumNoticeSpecification;
import com.billingcontext.domain.shared.finance.Money;
import com.billingcontext.domain.invoice.InvoiceId;
import com.billingcontext.domain.shared.finance.Percentage;
import com.billingcontext.domain.shared.id.DoctorId;
import com.billingcontext.domain.shared.id.PatientId;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.Currency;
import java.util.List;

public class TestFixtures {

    public static BigDecimal bigDecimalOf(String amount) {
        return new BigDecimal(amount).setScale(4, RoundingMode.HALF_EVEN);
    }
    public static Currency currencyOf(String currency) {
        return Currency.getInstance(currency);
    }
    public static Money moneyOf(String amount, String currency) {
        return new Money(new BigDecimal(amount), Currency.getInstance(currency));
    }
    public static Money moneyUSD(String amount){
        return new Money(new BigDecimal(amount), Currency.getInstance("USD"));
    }
    public static Money moneyEUR(String amount){
        return new Money(new BigDecimal(amount), Currency.getInstance("EUR"));
    }

    public static Percentage percentageOf(String amount) {
        return new Percentage(new BigDecimal(amount));
    }
    public static LocalDateTime localDateTimeOf( int month, int day) {
        return LocalDateTime.of(2026, month, day, 15, 0);
    }

    public static LocalDateTime localDateTimeOf(int day){
        return LocalDateTime.now().plusDays(day);
    }
    public static PricingEvaluationContext createPricingEvaluationContext(int creation, int due) {
        Instant now = Instant.parse("2026-07-04T12:00:00Z");
        Clock clock = Clock.fixed(now, ZoneOffset.UTC);
        LocalDateTime creationProforma = LocalDateTime.now(clock).minusMinutes(creation);
        LocalDateTime dueDate = LocalDateTime.now(clock).plusMinutes(due);

        return new PricingEvaluationContext(
                Money.moneyEUR(100),
                creationProforma,
                dueDate,
                clock
        );
    }
    public static List<DiscountRule> createDiscountRules() {
        DiscountRule immediateSettlementDiscountRuleTest = new ImmediateSettlementDiscountRule();
        DiscountRule earlyClearanceDiscountRule = new EarlyClearanceDiscountRule();
        DiscountRule promptPaymentDiscountRule = new PromptPaymentDiscountRule();
        return List.of(immediateSettlementDiscountRuleTest, earlyClearanceDiscountRule, promptPaymentDiscountRule);
    }
    public static DiscountPolicyService createDiscountPolicyService() {
        List<DiscountRule> discountRules = createDiscountRules();
        return new DiscountPolicyService(discountRules);
    }
    public static RefundEvaluationContext createRefundEvaluationContext(String initiator,
                                                                        long minutesAfterBooking,
                                                                        long minutesUntilAppointment) {
        return switch (initiator) {
            case "system" -> {
                CancellationInitiator systemInitiator = CancellationInitiator.SYSTEM_AUTOMATION;
                yield new RefundEvaluationContext(Money.moneyEUR(100.0),systemInitiator, minutesAfterBooking, minutesUntilAppointment);
            }
            case "patient" -> {
                CancellationInitiator patientInitiator = CancellationInitiator.PATIENT;
                yield new RefundEvaluationContext(Money.moneyEUR(100.0),patientInitiator, minutesAfterBooking, minutesUntilAppointment);
            }
            case "clinic" -> {
                CancellationInitiator clinicInitiator = CancellationInitiator.CLINIC_RECEPTION;
                yield new RefundEvaluationContext(Money.moneyEUR(100.0),clinicInitiator, minutesAfterBooking, minutesUntilAppointment);
            }
            default -> throw new IllegalArgumentException("initiator " + initiator + " not supported");
        };
    }
    public static List<RefundEligibilityRule> createRefundEligibilitySpecifications() {
        return List.of(
               new MinimumNoticeSpecification(),
               new ClinicInitiatedSpecification(),
               new BookingGracePeriodSpecification()
        );
    }
    public static RefundEvaluationContext createRefundEvaluationContext(Money totalPrice,
                                                                        String initiator,
                                                                        long minutesAfterBooking,
                                                                        long minutesUntilAppointment) {
        return switch (initiator) {
            case "system" -> {
                CancellationInitiator systemInitiator = CancellationInitiator.SYSTEM_AUTOMATION;
                yield new RefundEvaluationContext(totalPrice,systemInitiator, minutesAfterBooking, minutesUntilAppointment);
            }
            case "patient" -> {
                CancellationInitiator patientInitiator = CancellationInitiator.PATIENT;
                yield new RefundEvaluationContext(totalPrice,patientInitiator, minutesAfterBooking, minutesUntilAppointment);
            }
            case "clinic" -> {
                CancellationInitiator clinicInitiator = CancellationInitiator.CLINIC_RECEPTION;
                yield new RefundEvaluationContext(totalPrice,clinicInitiator, minutesAfterBooking, minutesUntilAppointment);
            }
            default -> throw new IllegalArgumentException("initiator " + initiator + " not supported");
        };
    }

    public static List<RefundCalculationStrategy> createRefundCalculationStrategies() {
        return List.of(
                new FullRefundStrategy(),
                new LatePenaltyDeductibleStrategy()
        );
    }
    public static RefundPolicyService createRefundPolicyService() {
        List<RefundEligibilityRule> refundEligibilityRules = createRefundEligibilitySpecifications();
        List<RefundCalculationStrategy> refundCalculationStrategies = createRefundCalculationStrategies();
        RefundEligibilityService refundEligibilityService = new RefundEligibilityService(refundEligibilityRules);
        RefundStrategyService refundStrategyService = new RefundStrategyService(refundCalculationStrategies);
        return new RefundPolicyService(refundStrategyService, refundEligibilityService);
    }



    public static AppointmentInvoice issueProforma(double amount, int minutesUntilAppointment, Clock clock) {
        AppointmentId appointmentId = new AppointmentId("appointment-123");
        DoctorId doctorId = new DoctorId("doctor-123");
        PatientId patientId = new PatientId("patient-123");
        Money basePrice = Money.moneyEUR(amount);
        LocalDateTime expiration = minutesUntilAppointment > 0 ? LocalDateTime.now(clock).plusMinutes(minutesUntilAppointment)
                : LocalDateTime.now(clock).minusMinutes(-minutesUntilAppointment);

        return AppointmentInvoice.issueProforma(appointmentId, doctorId, patientId, expiration, basePrice, clock);

    }
    public static Clock clock() {
        Instant now = Instant.parse("2026-07-04T12:00:00Z");
        return Clock.fixed(now, ZoneOffset.UTC);
    }
    public static Clock advanceTimeByMinutes(int minutes, Clock clock) {
        return Clock.offset(clock, Duration.ofMinutes(minutes));
    }
    public static PricingPolicyResult createPricingPolicyResult(double basePrice, double discountPercentage, double finalPrice) {
        return new PricingPolicyResult(
                Money.moneyEUR(basePrice),
                Percentage.of(discountPercentage),
                Percentage.of(10),
                Money.moneyEUR(finalPrice)
        );
    }
    public static Duration durationInMinutes(int minutes) {
        return Duration.ofMinutes(minutes);
    }
}
