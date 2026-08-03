package com.billingcontext.infrastructure.messaging.payment.initiate;


import com.billingcontext.application.usecase.payment.initiate.refund.RefundInitiationCommand;
import com.billingcontext.application.usecase.payment.initiate.refund.RefundInitiationUseCase;
import com.billingcontext.domain.invoice.RefundWasRequested;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class RefundRequestedEventListener {
    private final RefundInitiationUseCase refundInitiationUseCase;

    @EventListener
    public void onRefundRequested(RefundWasRequested event) {
        RefundInitiationCommand command = new RefundInitiationCommand(event.invoiceId(), event.refundAmount());
        refundInitiationUseCase.initializeRefund(command);
    }
}
