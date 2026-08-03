package com.billingcontext.application.usecase.payment.initiate.refund;

import com.billingcontext.application.repository.PaymentRepositoryPort;

import com.billingcontext.domain.payment.Payment;
import com.billingcontext.domain.payment.TransactionId;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Transactional
public class RefundInitiationService implements RefundInitiationUseCase {
    private final PaymentRepositoryPort paymentRepositoryPort;
    private final RefundGatewayPort refundGatewayPort;

    @Override
    public void initializeRefund(RefundInitiationCommand command) {
        Payment originalPayment = paymentRepositoryPort.findPaymentByInvoiceId(command.invoiceId());
        TransactionId refundId = refundGatewayPort.processRefund(
                command.toBeRefunded(),
                originalPayment.getTransactionId()
        );
        Payment refundLedger = Payment.refundCredit(originalPayment, command.toBeRefunded(), refundId);
        paymentRepositoryPort.save(refundLedger);
    }
}
