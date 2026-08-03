package com.billingcontext.domain.payment;


public enum PaymentStatus {
    PENDING,
    SUCCESS,
    FAILED,
    EXPIRED;


    public PaymentStatus changeStatusToSuccess() {
        if (this == PENDING) {
            return SUCCESS;
        }
        throw new PaymentStatusException(
                "Payment status can only be changed to SUCCESS from PENDING");
    }

    public PaymentStatus changeStatusToFail() {
        if (this == PENDING) {
            return FAILED;
        }
        throw new PaymentStatusException(
                "Payment status can only be changed to FAILED from PENDING");
    }
    public PaymentStatus changeStatusToExpired() {
        if (this == PENDING) {
            return EXPIRED;
        }
        throw new PaymentStatusException(
                "Payment status can only be changed to EXPIRED from PENDING");
    }
}