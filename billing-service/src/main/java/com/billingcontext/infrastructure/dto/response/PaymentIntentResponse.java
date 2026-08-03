package com.billingcontext.infrastructure.dto.response;

import com.billingcontext.domain.payment.TransactionId;

public record PaymentIntentResponse(String clientSecret, TransactionId transactionId) {
}
