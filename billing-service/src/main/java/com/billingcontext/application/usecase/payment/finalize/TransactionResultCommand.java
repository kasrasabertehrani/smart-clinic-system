package com.billingcontext.application.usecase.payment.finalize;

import com.billingcontext.domain.payment.PaymentStatus;
import com.billingcontext.domain.payment.TransactionId;
import com.billingcontext.domain.shared.finance.Money;

public record TransactionResultCommand(TransactionId transactionId, PaymentStatus paymentStatus
        , Money amountPaid, String failureReason) {}
