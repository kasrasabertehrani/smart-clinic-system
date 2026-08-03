package com.billingcontext.application.usecase.payment.initiate.checkout;

import com.billingcontext.domain.payment.PaymentId;
import com.billingcontext.domain.shared.finance.Money;
import com.billingcontext.infrastructure.dto.response.PaymentIntentResponse;

public interface PaymentGatewayPort {
    PaymentIntentResponse processPayment(Money amount);
}
