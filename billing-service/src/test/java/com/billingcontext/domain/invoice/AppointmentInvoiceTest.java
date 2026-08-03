package com.billingcontext.domain.invoice;

import com.billingcontext.domain.TestFixtures;

import com.billingcontext.domain.policy.pricing.PricingPolicyResult;
import com.billingcontext.domain.policy.refund.RefundPolicyResult;
import com.billingcontext.domain.shared.finance.Money;
import com.billingcontext.domain.shared.finance.Percentage;
import org.junit.jupiter.api.Test;


import java.time.Clock;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collection;


import static org.junit.jupiter.api.Assertions.*;

public class AppointmentInvoiceTest {
    private static final Clock CLOCK = TestFixtures.clock();
    private static final Duration GRACE_PERIOD = Duration.ofMinutes(15);


    @Test
    void testProformaCreation(){
        AppointmentInvoice proforma = TestFixtures.issueProforma(100.0 , 90, CLOCK);

        assertNotNull(proforma.getInvoiceId());
        assertNotNull(proforma.getAppointmentId());
        assertNotNull(proforma.getDoctorId());
        assertNotNull(proforma.getPatientId());
        assertEquals(LocalDateTime.now(CLOCK).plusMinutes(90), proforma.getExpirationDate());
        assertNull(proforma.getPaymentDurationWindow());
        assertEquals(Money.moneyEUR(100.0), proforma.getBasePrice());
        assertEquals(Percentage.of(0), proforma.getDiscountPercentage());
        assertEquals(Money.moneyEUR(100.0), proforma.getTotalAmount());
        assertEquals(Money.moneyEUR(0.0), proforma.getToBeRefunded());
        assertEquals(LocalDateTime.now(CLOCK), proforma.getCreatedAt());
        assertEquals(LocalDateTime.now(CLOCK), proforma.getUpdatedAt());
        assertEquals(InvoiceStatus.DRAFT, proforma.getInvoiceStatus());
    }
    @Test
    void testProformaCreationWithZeroBaseMoney(){
        assertThrows(InvoiceException.class, () -> {
            TestFixtures.issueProforma(0.0 , 90, CLOCK);
        });
    }
    @Test
    void testProformaCreationWithExpirationDateBeforeNow(){
        assertThrows(InvoiceException.class, () -> {
            TestFixtures.issueProforma(100.0 , -90, CLOCK);
        });
    }
    @Test
    void testUpdateProformaWhenInvoiceStatusIsDraft(){
        AppointmentInvoice proforma = TestFixtures.issueProforma(100.0 , 90, CLOCK);
        PricingPolicyResult policyResult = TestFixtures.createPricingPolicyResult(100.0, 10.0,  99.0);

        proforma.updateProforma(policyResult);

        assertEquals(proforma.getBasePrice(), policyResult.basePrice());
        assertEquals(proforma.getDiscountPercentage(), policyResult.discountPercentage());
        assertEquals(proforma.getTotalAmount(), policyResult.finalPrice());
    }
    @Test
    void testProcessCheckOutWhenInvoiceStatusIsDraft(){
        AppointmentInvoice proforma = TestFixtures.issueProforma(100.0 , 90, CLOCK);

        proforma.processCheckOut(GRACE_PERIOD, CLOCK);

        assertEquals(InvoiceStatus.PAYMENT_PENDING, proforma.getInvoiceStatus());
        assertEquals(LocalDateTime.now(CLOCK).plusMinutes(GRACE_PERIOD.toMinutes()), proforma.getPaymentDurationWindow());
    }
    @Test
    void testProcessCheckOutWhenInvoiceStatusIsDraftAndPaymentDurationWindowIsAfterExpirationDate(){
        AppointmentInvoice proforma = TestFixtures.issueProforma(100.0 , 90, CLOCK);
        Duration gracePeriod = TestFixtures.durationInMinutes(100);

        proforma.processCheckOut(gracePeriod, CLOCK);

        assertEquals(InvoiceStatus.PAYMENT_PENDING, proforma.getInvoiceStatus());
        assertEquals(proforma.getExpirationDate(), proforma.getPaymentDurationWindow());
    }
    @Test
    void testProcessCheckOutWhenInvoiceStatusIsPendingPayment(){
        AppointmentInvoice proforma = TestFixtures.issueProforma(100.0 , 90, CLOCK);
        Duration gracePeriod = TestFixtures.durationInMinutes(100);
        proforma.processCheckOut(gracePeriod, CLOCK);
        AppointmentInvoice pendingPaymentInvoice = proforma;

        pendingPaymentInvoice.processCheckOut(gracePeriod, CLOCK);

        assertEquals(InvoiceStatus.PAYMENT_PENDING, pendingPaymentInvoice.getInvoiceStatus());
    }
    @Test
    void testUpdateProformaWhenInvoiceIsInPendingPayment(){
        AppointmentInvoice proforma = TestFixtures.issueProforma(100.0 , 90, CLOCK);
        PricingPolicyResult policyResult = TestFixtures.createPricingPolicyResult(100.0, 10.0,  99.0);
        proforma.updateProforma(policyResult);
        proforma.processCheckOut(GRACE_PERIOD,CLOCK);
        PricingPolicyResult newPolicyResult = TestFixtures.createPricingPolicyResult(200.0, 10.0,  198.0);

        proforma.updateProforma(newPolicyResult);

        assertEquals(proforma.getBasePrice(), policyResult.basePrice());
        assertEquals(proforma.getDiscountPercentage(), policyResult.discountPercentage());
        assertEquals(proforma.getTotalAmount(), policyResult.finalPrice());
    }
    @Test
    void testFinalizePaymentWithMoneyEqualToTotalAmount(){
        AppointmentInvoice proforma = TestFixtures.issueProforma(100.0 , 90, CLOCK);
        PricingPolicyResult policyResult = TestFixtures.createPricingPolicyResult(100.0, 10.0,  99.0);
        proforma.updateProforma(policyResult);
        proforma.processCheckOut(GRACE_PERIOD,CLOCK);



        assertEquals(InvoiceStatus.PAID, proforma.getInvoiceStatus());
        assertEquals(Money.moneyEUR(99.0), proforma.getTotalAmount());
    }
    @Test
    void testFinalizePaymentWithMoneyDifferentFromTotalAmount() {
        AppointmentInvoice proforma = TestFixtures.issueProforma(100.0 , 90, CLOCK);
        PricingPolicyResult policyResult = TestFixtures.createPricingPolicyResult(100.0, 10.0,  99.0);
        proforma.updateProforma(policyResult);
        proforma.processCheckOut(GRACE_PERIOD,CLOCK);


    }
    @Test
    void testUpdateProformaWhenInvoiceStatusIsPaid(){
        AppointmentInvoice proforma = TestFixtures.issueProforma(100.0 , 90, CLOCK);
        PricingPolicyResult policyResult = TestFixtures.createPricingPolicyResult(100.0, 10.0,  99.0);
        proforma.updateProforma(policyResult);
        proforma.processCheckOut(GRACE_PERIOD,CLOCK);

        PricingPolicyResult newPolicyResult = TestFixtures.createPricingPolicyResult(200.0, 10.0,  198.0);

        assertThrows(InvoiceException.class, () -> {
            proforma.updateProforma(newPolicyResult);
        });
    }
    @Test
    void testProcessCheckOutWhenInvoiceStatusIsPaid() {
        AppointmentInvoice proforma = TestFixtures.issueProforma(100.0, 90, CLOCK);
        PricingPolicyResult policyResult = TestFixtures.createPricingPolicyResult(100.0, 10.0, 99.0);
        proforma.updateProforma(policyResult);
        proforma.processCheckOut(GRACE_PERIOD,CLOCK);


        assertThrows(InvoiceException.class, () -> {
            proforma.processCheckOut(GRACE_PERIOD,CLOCK);
        });
    }
    @Test
    void testProcessCancellationWhenInvoiceStatusIsDraft(){
        AppointmentInvoice proforma = TestFixtures.issueProforma(100.0 , 90, CLOCK);
        RefundPolicyResult refundPolicyResult = RefundPolicyResult.approved(Money.moneyEUR(50.0));

        proforma.processCancellation(refundPolicyResult);

        assertEquals(InvoiceStatus.CANCELLED, proforma.getInvoiceStatus());
        assertNull(proforma.getPaymentDurationWindow());
        assertEquals(Money.zero(proforma.getBasePrice().currency()), proforma.getToBeRefunded());
    }
    @Test
    void testProcessCancellationWhenInvoiceStatusIsPaid(){
        AppointmentInvoice proforma = TestFixtures.issueProforma(100.0 , 90, CLOCK);
        PricingPolicyResult policyResult = TestFixtures.createPricingPolicyResult(100.0, 10.0,  99.0);
        proforma.updateProforma(policyResult);
        proforma.processCheckOut(GRACE_PERIOD,CLOCK);

        RefundPolicyResult refundPolicyResult = RefundPolicyResult.approved(Money.moneyEUR(50.0));



        assertEquals(InvoiceStatus.REFUND_PENDING, proforma.getInvoiceStatus());
        assertEquals(Money.moneyEUR(50.0), proforma.getToBeRefunded());

    }
    @Test
    void testProcessCancellationWhenInvoiceIsNotEligibleForRefund(){
        AppointmentInvoice proforma = TestFixtures.issueProforma(100.0 , 90, CLOCK);
        PricingPolicyResult policyResult = TestFixtures.createPricingPolicyResult(100.0, 10.0,  99.0);
        proforma.updateProforma(policyResult);
        proforma.processCheckOut(GRACE_PERIOD,CLOCK);

        RefundPolicyResult refundPolicyResult = RefundPolicyResult.denied("test reason", proforma.getBasePrice().currency());

        proforma.processCancellation(refundPolicyResult);

        assertEquals(InvoiceStatus.CANCELLED, proforma.getInvoiceStatus());
        assertNull(proforma.getPaymentDurationWindow());
        assertEquals(Money.zero(proforma.getBasePrice().currency()), proforma.getToBeRefunded());
    }
    @Test
    void testProcessCancellationWhenRefundAmountIsMoreThanBasePrice(){
        AppointmentInvoice proforma = TestFixtures.issueProforma(100.0 , 90, CLOCK);
        PricingPolicyResult policyResult = TestFixtures.createPricingPolicyResult(100.0, 10.0,  99.0);
        proforma.updateProforma(policyResult);
        proforma.processCheckOut(GRACE_PERIOD,CLOCK);

        RefundPolicyResult refundPolicyResult = RefundPolicyResult.approved(Money.moneyEUR(150.0));

       var e = assertThrows(InvoiceException.class, () -> {
            proforma.processCancellation(refundPolicyResult);
        });
       assertEquals("Refund amount cannot exceed the total amount paid.", e.getMessage());
    }
    @Test
    void testProcessCancellationWhenInvoiceStatusIsPaymentPending(){
        AppointmentInvoice proforma = TestFixtures.issueProforma(100.0 , 90, CLOCK);
        PricingPolicyResult policyResult = TestFixtures.createPricingPolicyResult(100.0, 10.0,  99.0);
        proforma.updateProforma(policyResult);
        proforma.processCheckOut(GRACE_PERIOD,CLOCK);
        RefundPolicyResult refundPolicyResult = RefundPolicyResult.denied("test reason", proforma.getBasePrice().currency());

        var e = assertThrows(InvoiceException.class, () -> {
            proforma.processCancellation(refundPolicyResult);
        });

        assertEquals("Cannot process cancellation for invoice in state: " + proforma.getInvoiceStatus(), e.getMessage());
    }
    @Test
    void testProcessCancellationWhenInvoiceStatusIsRefundPending(){
        AppointmentInvoice proforma = TestFixtures.issueProforma(100.0 , 90, CLOCK);
        PricingPolicyResult policyResult = TestFixtures.createPricingPolicyResult(100.0, 10.0,  99.0);
        proforma.updateProforma(policyResult);
        proforma.processCheckOut(GRACE_PERIOD,CLOCK);

        RefundPolicyResult refundPolicyResult = RefundPolicyResult.approved(Money.moneyEUR(50.0));
        proforma.processCancellation(refundPolicyResult);

        RefundPolicyResult newRefundPolicyResult = RefundPolicyResult.approved(Money.moneyEUR(30.0));
        proforma.processCancellation(newRefundPolicyResult);


        assertEquals(InvoiceStatus.REFUND_PENDING, proforma.getInvoiceStatus());
        assertEquals(refundPolicyResult.refundAmount(), proforma.getToBeRefunded());

    }
    @Test
    void testProcessCancellationWhenInvoiceStatusIsExpired(){
        AppointmentInvoice proforma = TestFixtures.issueProforma(100.0 , 90, CLOCK);

        RefundPolicyResult refundPolicyResult = RefundPolicyResult.approved(Money.moneyEUR(50.0));

        proforma.processCancellation(refundPolicyResult);

        assertEquals(InvoiceStatus.EXPIRED, proforma.getInvoiceStatus());
    }
    @Test
    void testFinalizeRefund(){
        AppointmentInvoice proforma = TestFixtures.issueProforma(100.0 , 90, CLOCK);
        PricingPolicyResult policyResult = TestFixtures.createPricingPolicyResult(100.0, 10.0,  99.0);
        proforma.updateProforma(policyResult);
        proforma.processCheckOut(GRACE_PERIOD,CLOCK);

        RefundPolicyResult refundPolicyResult = RefundPolicyResult.approved(Money.moneyEUR(50.0));
        proforma.processCancellation(refundPolicyResult);



        assertEquals(InvoiceStatus.REFUNDED, proforma.getInvoiceStatus());
    }
    @Test
    void testFinalizeRefundWhenAmountToBeRefundedIsNotTheSameAsInInvoice(){
        AppointmentInvoice proforma = TestFixtures.issueProforma(100.0 , 90, CLOCK);
        PricingPolicyResult policyResult = TestFixtures.createPricingPolicyResult(100.0, 10.0,  99.0);
        proforma.updateProforma(policyResult);
        proforma.processCheckOut(GRACE_PERIOD,CLOCK);

        RefundPolicyResult refundPolicyResult = RefundPolicyResult.approved(Money.moneyEUR(50.0));
        proforma.processCancellation(refundPolicyResult);


    }
    @Test
    void testExpireInvoiceWhenExpirationDateHasNotPassedAndInvoiceStatusIsDraft(){
        AppointmentInvoice proforma = TestFixtures.issueProforma(100.0 , 90, CLOCK);



        assertEquals(InvoiceStatus.DRAFT, proforma.getInvoiceStatus());
    }
    @Test
    void testExpireInvoiceWhenExpirationDateHasNotPassedAndInvoiceStatusIsPaymentPending(){
        AppointmentInvoice proforma = TestFixtures.issueProforma(100.0 , 90, CLOCK);
        PricingPolicyResult policyResult = TestFixtures.createPricingPolicyResult(100.0, 10.0,  99.0);
        proforma.updateProforma(policyResult);
        proforma.processCheckOut(GRACE_PERIOD,CLOCK);



        assertEquals(InvoiceStatus.PAYMENT_PENDING, proforma.getInvoiceStatus());
    }
    @Test
    void testExpireInvoiceWhenExpirationDateHasNotPassedAndInvoiceStatusIsPaid(){
        AppointmentInvoice proforma = TestFixtures.issueProforma(100.0 , 90, CLOCK);
        PricingPolicyResult policyResult = TestFixtures.createPricingPolicyResult(100.0, 10.0,  99.0);
        proforma.updateProforma(policyResult);
        proforma.processCheckOut(GRACE_PERIOD,CLOCK);




        assertEquals(InvoiceStatus.PAID, proforma.getInvoiceStatus());
    }
    @Test
    void testExpireInvoiceWhenExpirationDateHasPassedAndInvoiceStatusIsDraft(){
        AppointmentInvoice proforma = TestFixtures.issueProforma(100.0 , 90, CLOCK);




        assertEquals(InvoiceStatus.EXPIRED, proforma.getInvoiceStatus());

    }
    @Test
    void testExpireInvoiceWhenExpirationDateHasPassedAndInvoiceStatusIsPaymentPending(){
        AppointmentInvoice proforma = TestFixtures.issueProforma(100.0 , 90, CLOCK);
        PricingPolicyResult policyResult = TestFixtures.createPricingPolicyResult(100.0, 10.0,  99.0);
        proforma.updateProforma(policyResult);
        proforma.processCheckOut(GRACE_PERIOD,CLOCK);


    }
    @Test
    void testExpireInvoiceWhenExpirationDateHasPassedAndInvoiceStatusIsPaid(){
        AppointmentInvoice proforma = TestFixtures.issueProforma(100.0 , 90, CLOCK);
        PricingPolicyResult policyResult = TestFixtures.createPricingPolicyResult(100.0, 10.0,  99.0);
        proforma.updateProforma(policyResult);
        proforma.processCheckOut(GRACE_PERIOD,CLOCK);




        assertEquals(InvoiceStatus.PAID, proforma.getInvoiceStatus());
    }
    @Test
    void testHandlePaymentGracePeriodTimeOutWhenInvoiceStatusIsDraft(){
        AppointmentInvoice proforma = TestFixtures.issueProforma(100.0 , 90, CLOCK);



        assertEquals(InvoiceStatus.DRAFT, proforma.getInvoiceStatus());
    }
    @Test
    void testHandlePaymentGracePeriodTimeOutWhenInvoiceStatusIsPaymentPendingAndPaymentGracePeriodHasNotPassed(){
        AppointmentInvoice proforma = TestFixtures.issueProforma(100.0 , 90, CLOCK);
        PricingPolicyResult policyResult = TestFixtures.createPricingPolicyResult(100.0, 10.0,  99.0);
        proforma.updateProforma(policyResult);
        proforma.processCheckOut(GRACE_PERIOD,CLOCK);



        assertEquals(InvoiceStatus.PAYMENT_PENDING, proforma.getInvoiceStatus());
    }
    @Test
    void testHandlePaymentGracePeriodTimeOutWhenInvoiceStatusIsPaymentPendingAndPaymentGracePeriodHasPassed(){
        AppointmentInvoice proforma = TestFixtures.issueProforma(100.0 , 90, CLOCK);
        PricingPolicyResult policyResult = TestFixtures.createPricingPolicyResult(100.0, 10.0,  99.0);
        proforma.updateProforma(policyResult);
        proforma.processCheckOut(GRACE_PERIOD,CLOCK);



        assertEquals(InvoiceStatus.DRAFT, proforma.getInvoiceStatus());
        assertNull(proforma.getPaymentDurationWindow());
        assertEquals(Percentage.zero(), proforma.getDiscountPercentage());
        assertEquals(proforma.getBasePrice(), proforma.getTotalAmount());
    }
    @Test
    void testHandlePaymentGracePeriodWhenInvoiceIsPaid(){
        AppointmentInvoice proforma = TestFixtures.issueProforma(100.0 , 90, CLOCK);
        PricingPolicyResult policyResult = TestFixtures.createPricingPolicyResult(100.0, 10.0,  99.0);
        proforma.updateProforma(policyResult);
        proforma.processCheckOut(GRACE_PERIOD,CLOCK);




        assertEquals(InvoiceStatus.PAID, proforma.getInvoiceStatus());
    }

}
