package com.billingcontext.domain.payment;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class PaymentStatusTest {
    @Test
    void testChangeStatusToSuccess() {
        PaymentStatus paymentStatus = PaymentStatus.PENDING;
        PaymentStatus newStatus = paymentStatus.changeStatusToSuccess();
        assert newStatus == PaymentStatus.SUCCESS;
    }
    @Test
    void testChangeStatusToSuccessWithInvalidPreviousState() {
        PaymentStatus paymentStatus = PaymentStatus.FAILED;
        assertThrows(PaymentStatusException.class, paymentStatus::changeStatusToSuccess);
    }
    @Test
    void testChangeStatusToFailure() {
        PaymentStatus paymentStatus = PaymentStatus.PENDING;
        PaymentStatus newStatus = paymentStatus.changeStatusToFail();
        assert newStatus == PaymentStatus.FAILED;
    }
    @Test
    void testChangeStatusToFailureWithInvalidPreviousState() {
        PaymentStatus paymentStatus = PaymentStatus.SUCCESS;
        assertThrows(PaymentStatusException.class, paymentStatus::changeStatusToFail);
    }
}
