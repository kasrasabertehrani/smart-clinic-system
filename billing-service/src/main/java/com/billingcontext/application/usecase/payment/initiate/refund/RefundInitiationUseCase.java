package com.billingcontext.application.usecase.payment.initiate.refund;


import com.billingcontext.domain.payment.TransactionId;

public interface RefundInitiationUseCase {
    void initializeRefund(RefundInitiationCommand command);
}
