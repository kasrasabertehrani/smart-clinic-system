package com.billingcontext.application.usecase.payment.initiate.checkout;

import com.billingcontext.domain.invoice.InvoiceId;
import com.billingcontext.infrastructure.dto.response.PaymentInitializationResponse;

public interface InitiateCheckoutUseCase {
    PaymentInitializationResponse initializePayment(InvoiceId invoiceId);
}
