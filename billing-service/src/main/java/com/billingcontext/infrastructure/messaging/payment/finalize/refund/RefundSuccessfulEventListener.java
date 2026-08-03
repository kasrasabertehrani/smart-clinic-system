package com.billingcontext.infrastructure.messaging.payment.finalize.refund;

import com.billingcontext.application.usecase.payment.finalize.FinalizeTransactionUseCase;
import com.billingcontext.application.usecase.payment.finalize.InvoicePaymentResultCommand;


import com.billingcontext.domain.payment.RefundWasSuccessful;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class RefundSuccessfulEventListener {

    private final FinalizeTransactionUseCase finalizeTransactionUseCase;

    @EventListener
    public void onRefundSuccessful(RefundWasSuccessful event) {
        finalizeTransactionUseCase.finalizeTransactionForInvoice(new InvoicePaymentResultCommand(event.invoiceId(), event.amountOwed()));
    }
}
