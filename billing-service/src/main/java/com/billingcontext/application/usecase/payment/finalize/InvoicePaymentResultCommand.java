package com.billingcontext.application.usecase.payment.finalize;

import com.billingcontext.domain.invoice.InvoiceId;
import com.billingcontext.domain.shared.finance.Money;

public record InvoicePaymentResultCommand(InvoiceId invoiceId, Money amountPaid) {
}
