package com.billingcontext.domain.invoice;

public enum InvoiceStatus {
    DRAFT,
    PAYMENT_PENDING,
    PAID,
    REFUND_PENDING,
    REFUNDED,
    CANCELLED,
    EXPIRED;


    public InvoiceStatus changeStatusToDraft(){
        if (this == PAYMENT_PENDING) {
            return DRAFT;
        }
        throw new InvoiceStatusException(
                "Invoice status can only be changed to DRAFT from PAYMENT_PENDING");
    }
    public InvoiceStatus changeStatusToPaymentPending(){
        if (this == DRAFT) {
            return PAYMENT_PENDING;
        }
        throw new InvoiceStatusException(
                "Invoice status can only be changed to PAYMENT_PENDING from DRAFT");
    }

    public InvoiceStatus changeStatusToPaid() {
        if (this == PAYMENT_PENDING) {
            return PAID;
        }
        throw new InvoiceStatusException(
                "Invoice status can only be changed to PAID from PAYMENT_PENDING");
    }
    public InvoiceStatus changeStatusToRefundPending() {
        if (this == PAID) {
            return REFUND_PENDING;
        }
        throw new InvoiceStatusException(
                "Invoice status can only be changed to REFUND_PENDING from PAID");
    }

    public InvoiceStatus changeStatusToRefunded() {
        if (this == REFUND_PENDING) {
            return REFUNDED;
        }
        throw new InvoiceStatusException(
                "Payment status can only be changed to REFUNDED from REFUND_PENDING");
    }
    public InvoiceStatus changeStatusToCancelled() {
        if (this == PAID || this == DRAFT) {
            return CANCELLED;
        }
        throw new InvoiceStatusException(
                "Payment status can only be changed to CANCELLED from PAID Or DRAFT");
    }
    public InvoiceStatus changeStatusToExpired() {
        if (this == DRAFT || this == PAYMENT_PENDING) {
            return EXPIRED;
        }
        throw new InvoiceStatusException(
                "Payment status can only be changed to EXPIRED from DRAFT or  PAYMENT_PENDING");
    }


}
