package com.billingcontext.infrastructure.messaging.payment.finalize.checkout;

import com.billingcontext.application.usecase.payment.finalize.FinalizeTransactionUseCase;
import com.billingcontext.application.usecase.payment.finalize.InvoicePaymentResultCommand;
import com.billingcontext.domain.payment.PaymentWasSuccessful;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class PaymentSuccessfulEventListener {
    private final FinalizeTransactionUseCase finalizeTransactionUseCase;

    @EventListener
    public void onPaymentSuccessful(PaymentWasSuccessful event) {
        InvoicePaymentResultCommand command = new InvoicePaymentResultCommand(
                event.invoiceId(),
                event.amountOwed()
        );
        finalizeTransactionUseCase.finalizeTransactionForInvoice(command);
    }
}
