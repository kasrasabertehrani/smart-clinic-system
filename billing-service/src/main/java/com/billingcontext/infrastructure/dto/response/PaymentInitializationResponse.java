package com.billingcontext.infrastructure.dto.response;

import com.billingcontext.domain.invoice.AppointmentInvoice;
import com.billingcontext.domain.payment.Payment;

import java.time.LocalDateTime;

public record PaymentInitializationResponse(
        String invoiceId,
        String paymentId,
        long amount,
        String currency,
        LocalDateTime paymentDurationWindow,
        String paymentType,
        String invoiceStatus,
        String paymentStatus,
        String clientSecret
        ) {

    public static PaymentInitializationResponse from(AppointmentInvoice invoice, Payment ledger, String clientSecret){
        return new PaymentInitializationResponse(
                invoice.getInvoiceId().value(),
                ledger.getPaymentId().value(),
                ledger.getAmountOwed().toCents(),
                ledger.getAmountOwed().currency().toString(),
                invoice.getPaymentDurationWindow(),
                ledger.getPaymentType().toString(),
                invoice.getInvoiceStatus().name(),
                ledger.getPaymentStatus().name(),
                clientSecret
        );
    }
}
