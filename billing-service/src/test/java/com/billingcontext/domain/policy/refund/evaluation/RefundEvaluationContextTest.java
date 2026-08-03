package com.billingcontext.domain.policy.refund.evaluation;

import com.billingcontext.domain.shared.finance.Money;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RefundEvaluationContextTest {

    @Test
    void testRefundEvaluationContextCreation(){
        CancellationInitiator initiator = CancellationInitiator.CLINIC_RECEPTION;
        long durationSinceBooking = 50L;
        long minutesUntilAppointment = 30L;
        Money money = Money.moneyEUR(80.0);

        RefundEvaluationContext refundEvaluationContext = new RefundEvaluationContext(money,initiator,
                durationSinceBooking, minutesUntilAppointment);

        assertEquals(initiator, refundEvaluationContext.initiator());
        assertEquals(durationSinceBooking, refundEvaluationContext.durationSinceBooking());
        assertEquals(minutesUntilAppointment, refundEvaluationContext.minutesUntilAppointment());
    }
    @Test
    void testRefundEvaluationContextWithInvalidData(){
        assertThrows(RefundEvaluationContextException.class, () -> {
            new RefundEvaluationContext(Money.moneyEUR(80.0),null, 50L, 30L);
        });

        assertThrows(RefundEvaluationContextException.class, () -> {
            new RefundEvaluationContext(Money.moneyEUR(80.0),CancellationInitiator.PATIENT, -10L, 30L);
        });

        assertThrows(RefundEvaluationContextException.class, () -> {
            new RefundEvaluationContext(Money.moneyEUR(80.0),CancellationInitiator.PATIENT, 50L, -5L);
        });
    }
    @Test
    void testIsCanceledBySystemAutomation(){
        CancellationInitiator initiator = CancellationInitiator.PATIENT;
        CancellationInitiator systemAutomation = CancellationInitiator.SYSTEM_AUTOMATION;
        long durationSinceBooking = 50L;
        long minutesUntilAppointment = 30L;

        RefundEvaluationContext refundEvaluationContext = new RefundEvaluationContext(Money.moneyEUR(80.0),initiator,
                durationSinceBooking, minutesUntilAppointment);
        RefundEvaluationContext refundEvaluationContext1 = new RefundEvaluationContext(Money.moneyEUR(80.0),systemAutomation,
                durationSinceBooking, minutesUntilAppointment);

        assertFalse(refundEvaluationContext.isCanceledBySystemAutomation());
        assertTrue(refundEvaluationContext1.isCanceledBySystemAutomation());

    }
    @Test
    void testIsCanceledByClinicStaff(){
        CancellationInitiator initiator = CancellationInitiator.PATIENT;
        CancellationInitiator clinicReception = CancellationInitiator.CLINIC_RECEPTION;
        long durationSinceBooking = 50L;
        long minutesUntilAppointment = 30L;

        RefundEvaluationContext refundEvaluationContext = new RefundEvaluationContext(Money.moneyEUR(80.0),initiator,
                durationSinceBooking, minutesUntilAppointment);
        RefundEvaluationContext refundEvaluationContext1 = new RefundEvaluationContext(Money.moneyEUR(80.0),clinicReception,
                durationSinceBooking, minutesUntilAppointment);

        assertFalse(refundEvaluationContext.isCanceledByClinicStaff());
        assertTrue(refundEvaluationContext1.isCanceledByClinicStaff());
    }
    @Test
    void testIsCanceledByPatient(){
        CancellationInitiator patient = CancellationInitiator.PATIENT;
        CancellationInitiator clinicReception = CancellationInitiator.CLINIC_RECEPTION;
        long durationSinceBooking = 50L;
        long minutesUntilAppointment = 30L;

        RefundEvaluationContext refundEvaluationContext = new RefundEvaluationContext(Money.moneyEUR(80.0),patient,
                durationSinceBooking, minutesUntilAppointment);
        RefundEvaluationContext refundEvaluationContext1 = new RefundEvaluationContext(Money.moneyEUR(80.0),clinicReception,
                durationSinceBooking, minutesUntilAppointment);

        assertTrue(refundEvaluationContext.isCanceledByPatient());
        assertFalse(refundEvaluationContext1.isCanceledByPatient());
    }
}
