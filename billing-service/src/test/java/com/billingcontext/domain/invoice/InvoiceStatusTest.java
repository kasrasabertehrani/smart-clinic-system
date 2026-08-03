package com.billingcontext.domain.invoice;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class InvoiceStatusTest {
    @Test
    void testChangeInvoiceStatusToPaidWithValidPreviousState() {
        InvoiceStatus invoiceStatus = InvoiceStatus.PAYMENT_PENDING;
        InvoiceStatus newStatus = invoiceStatus.changeStatusToPaid();
        assert newStatus == InvoiceStatus.PAID;
    }
    @Test
    void testChangeInvoiceStatusToPaidWithInValidPreviousState() {
        InvoiceStatus invoiceStatus = InvoiceStatus.CANCELLED;

        assertThrows(InvoiceStatusException.class, invoiceStatus::changeStatusToPaid);
    }
    @Test
    void testChangeInvoiceStatusToRefundPendingWithValidPreviousState() {
        InvoiceStatus invoiceStatus = InvoiceStatus.PAID;
        InvoiceStatus newStatus = invoiceStatus.changeStatusToRefundPending();
        assert newStatus == InvoiceStatus.REFUND_PENDING;
    }
    @Test
    void testChangeInvoiceStatusToRefundPendingWithInvalidPreviousState() {
        InvoiceStatus invoiceStatus = InvoiceStatus.CANCELLED;

        assertThrows(InvoiceStatusException.class, invoiceStatus::changeStatusToPaid);
    }
    @Test
    void testChangeInvoiceStatusToRefundedWithValidPreviousState() {
        InvoiceStatus invoiceStatus = InvoiceStatus.REFUND_PENDING;
        InvoiceStatus newStatus = invoiceStatus.changeStatusToRefunded();
        assert newStatus == InvoiceStatus.REFUNDED;
    }
    @Test
    void testChangeInvoiceStatusToRefundedWithInvalidPreviousState() {
        InvoiceStatus invoiceStatus = InvoiceStatus.PAID;
        assertThrows(InvoiceStatusException.class, invoiceStatus::changeStatusToRefunded);
    }
    @Test
    void testChangeInvoiceStatusToCancelledWithValidPreviousStateOfPaid() {
        InvoiceStatus invoiceStatus = InvoiceStatus.PAID;
        InvoiceStatus newStatus = invoiceStatus.changeStatusToCancelled();
        assert newStatus == InvoiceStatus.CANCELLED;
    }
    @Test
    void testChangeInvoiceStatusToCancelledWithValidPreviousStateOfPending() {
        InvoiceStatus invoiceStatus = InvoiceStatus.DRAFT;
        InvoiceStatus newStatus = invoiceStatus.changeStatusToCancelled();
        assert newStatus == InvoiceStatus.CANCELLED;
    }
    @Test
    void testChangeInvoiceStatusToCancelledWithValidPreviousStateOfPendingPayment() {
        InvoiceStatus invoiceStatus = InvoiceStatus.PAYMENT_PENDING;
        InvoiceStatus newStatus = invoiceStatus.changeStatusToCancelled();
        assert newStatus == InvoiceStatus.CANCELLED;
    }
    @Test
    void testChangeInvoiceStatusToCancelledWithInvalidPreviousState() {
        InvoiceStatus invoiceStatus = InvoiceStatus.REFUNDED;
        assertThrows(InvoiceStatusException.class, invoiceStatus::changeStatusToCancelled);
    }
    @Test
    void testChangeInvoiceStatusToExpiredWithValidPreviousState() {

    }
    @Test
    void testChangeInvoiceStatusToExpiredWithInvalidPreviousState() {
        InvoiceStatus invoiceStatus = InvoiceStatus.REFUNDED;
        assertThrows(InvoiceStatusException.class, invoiceStatus::changeStatusToExpired);
    }
}
