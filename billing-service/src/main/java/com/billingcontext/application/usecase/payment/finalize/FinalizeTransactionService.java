package com.billingcontext.application.usecase.payment.finalize;

import com.billingcontext.application.repository.InvoiceRepositoryPort;
import com.billingcontext.application.repository.PaymentRepositoryPort;
import com.billingcontext.domain.invoice.AppointmentInvoice;
import com.billingcontext.domain.payment.Payment;
import com.billingcontext.domain.payment.TransactionResult;
import com.billingcontext.infrastructure.dto.response.TransactionResultResponse;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Transactional
public class FinalizeTransactionService implements FinalizeTransactionUseCase {
    private final InvoiceRepositoryPort invoiceRepositoryPort;
    private final PaymentRepositoryPort paymentRepositoryPort;
    private final ApplicationEventPublisher eventPublisher;



    @Override
    public TransactionResultResponse finalizeTransactionForLedger(TransactionResultCommand command) {
        Payment ledger = paymentRepositoryPort.findByTransactionId(command.transactionId());
        TransactionResult result = new TransactionResult(
                ledger.getPaymentType(),
                command.amountPaid(),
                command.paymentStatus(),
                command.failureReason()
        );
        ledger.finalizePaymentStatus(result);
        paymentRepositoryPort.save(ledger);
        ledger.getDomainEvents().forEach(eventPublisher::publishEvent);
        ledger.clearDomainEvents();
        return new TransactionResultResponse(
                ledger.getPaymentId().value(),
                ledger.getPaymentType(),
                ledger.getPaymentStatus(),
                command.failureReason()
        );
    }

    @Override
    public void finalizeTransactionForInvoice(InvoicePaymentResultCommand command) {
        AppointmentInvoice invoice =  invoiceRepositoryPort.findInvoiceById(command.invoiceId());
        invoice.confirmTransaction(command.amountPaid());
        invoiceRepositoryPort.save(invoice);
    }
}
