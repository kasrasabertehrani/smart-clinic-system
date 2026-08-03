package com.billingcontext.application.usecase.payment.initiate.refund;


import com.billingcontext.domain.payment.TransactionId;
import com.billingcontext.domain.shared.finance.Money;

public interface RefundGatewayPort {
    TransactionId processRefund(Money refundAmount, TransactionId originalTransactionId);
}
