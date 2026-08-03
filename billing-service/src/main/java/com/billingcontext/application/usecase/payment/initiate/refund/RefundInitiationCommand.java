package com.billingcontext.application.usecase.payment.initiate.refund;

import com.billingcontext.domain.invoice.InvoiceId;
import com.billingcontext.domain.shared.finance.Money;

public record RefundInitiationCommand(InvoiceId invoiceId, Money toBeRefunded) {
}
