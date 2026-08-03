package com.billingcontext.infrastructure.dto.response;

import com.billingcontext.domain.payment.Payment;
import com.billingcontext.domain.payment.PaymentStatus;

public record TransactionResultResponse(String paymentId,
                                        Payment.PaymentType paymentType,
                                        PaymentStatus paymentStatus,
                                        String failedReason) {
}
