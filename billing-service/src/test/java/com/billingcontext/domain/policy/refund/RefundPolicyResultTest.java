package com.billingcontext.domain.policy.refund;

import com.billingcontext.domain.shared.finance.Money;
import org.junit.jupiter.api.Test;

import java.util.Currency;

import static org.junit.jupiter.api.Assertions.*;

public class RefundPolicyResultTest {

    @Test
    void testRefundPolicyResultCreation() {
        RefundPolicyResult result = new RefundPolicyResult(true, Money.moneyEUR(50.0), null);

        assertTrue(result.isEligible());
        assertEquals(Money.moneyEUR(50.0), result.refundAmount());
        assertNull(result.rejectionReason());
    }
    @Test
    void shouldThrowException_WhenRefundAmountIsNull() {
        RefundPolicyResultException exception = assertThrows(RefundPolicyResultException.class, () -> {
            new RefundPolicyResult(true, null, null);
        });

        assertEquals("refundAmount is null", exception.getMessage());
    }

    @Test
    void shouldThrowException_WhenApprovedRefundHasRejectionReason() {
        RefundPolicyResultException exception = assertThrows(RefundPolicyResultException.class, () -> {
            new RefundPolicyResult(true, Money.moneyEUR(50.0), "Customer complained");
        });

        assertEquals("An approved refund cannot have a rejection reason.", exception.getMessage());
    }

    @Test
    void shouldThrowException_WhenDeniedRefundIsMissingRejectionReason() {
        RefundPolicyResultException exception = assertThrows(RefundPolicyResultException.class, () -> {
            new RefundPolicyResult(false, Money.moneyEUR(0.0), null);
        });

        assertEquals("A denied refund must have a rejection reason.", exception.getMessage());
    }

    @Test
    void shouldThrowException_WhenDeniedRefundHasNonZeroAmount() {
        RefundPolicyResultException exception = assertThrows(RefundPolicyResultException.class, () -> {
            // Note: We provide a reason here, otherwise it triggers the missing reason guard first!
            new RefundPolicyResult(false, Money.moneyEUR(50.0), "Too late to cancel");
        });

        assertEquals("A denied refund cannot have a monetary value greater than zero.", exception.getMessage());
    }

    @Test
    void shouldThrowException_WhenApprovedRefundHasZeroAmount() {
        RefundPolicyResultException exception = assertThrows(RefundPolicyResultException.class, () -> {
            new RefundPolicyResult(true, Money.moneyEUR(0.0), null);
        });

        // Note: You have a copy-paste error in your domain class!
        // It throws about a "category" even though it's checking for a zero amount.
        assertEquals("An approved refund must specify the approved category.", exception.getMessage());
    }
    @Test
    void testApproveRefund() {
        RefundPolicyResult approved = RefundPolicyResult.approved(Money.moneyEUR(50.0));
        assertTrue(approved.isEligible());
        assertEquals(Money.moneyEUR(50.0), approved.refundAmount());
        assertNull(approved.rejectionReason());
    }
    @Test
    void testApproveRefundWithRejectionReason() {
        RefundPolicyResult approved = RefundPolicyResult.denied("Too late to cancel", Currency.getInstance("EUR"));
        assertFalse(approved.isEligible());
        assertEquals(Money.moneyEUR(0.0), approved.refundAmount());
        assertEquals("Too late to cancel", approved.rejectionReason());

    }
}
