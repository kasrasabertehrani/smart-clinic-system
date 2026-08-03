package com.billingcontext.application.usecase.payment.finalize;


import com.billingcontext.infrastructure.dto.response.TransactionResultResponse;

public interface FinalizeTransactionUseCase {
    TransactionResultResponse finalizeTransactionForLedger(TransactionResultCommand command);
    void finalizeTransactionForInvoice(InvoicePaymentResultCommand command);
}
