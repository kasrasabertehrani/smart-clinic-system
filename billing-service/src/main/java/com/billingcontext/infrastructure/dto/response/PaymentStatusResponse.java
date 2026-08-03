package com.billingcontext.infrastructure.dto.response;


import com.billingcontext.domain.payment.Payment;
import com.billingcontext.domain.payment.PaymentStatus;

import java.math.BigDecimal;

public record PaymentStatusResponse(
        String paymentId,
        Payment.PaymentType paymentType,
        PaymentStatus paymentStatus,
        BigDecimal amount,
        String currency) {


}
