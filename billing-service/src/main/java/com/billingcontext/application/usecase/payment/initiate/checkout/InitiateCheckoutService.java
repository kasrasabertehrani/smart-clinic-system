package com.billingcontext.application.usecase.payment.initiate.checkout;

import com.billingcontext.application.repository.InvoiceRepositoryPort;
import com.billingcontext.application.repository.PaymentRepositoryPort;
import com.billingcontext.domain.invoice.AppointmentInvoice;
import com.billingcontext.domain.invoice.InvoiceId;
import com.billingcontext.domain.payment.Payment;
import com.billingcontext.infrastructure.dto.response.PaymentInitializationResponse;

import com.billingcontext.infrastructure.dto.response.PaymentIntentResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.Duration;
@Service
@Transactional
public class InitiateCheckoutService implements InitiateCheckoutUseCase {

    private final InvoiceRepositoryPort invoiceRepositoryPort;
    private final PaymentRepositoryPort paymentRepositoryPort;
    private final PaymentGatewayPort gateway;
    private final Duration paymentTimeout;
    private final Clock clock;

    public InitiateCheckoutService(
            InvoiceRepositoryPort invoiceRepositoryPort,
            PaymentRepositoryPort paymentRepositoryPort,
            PaymentGatewayPort gateway,
            @Value("${clinic.checkout.payment-timeout:15m}") Duration paymentTimeout,
            Clock clock) {
        this.invoiceRepositoryPort = invoiceRepositoryPort;
        this.paymentRepositoryPort = paymentRepositoryPort;
        this.gateway = gateway;
        this.paymentTimeout = paymentTimeout;
        this.clock = clock;
    }


    @Override
    public PaymentInitializationResponse initializePayment(InvoiceId invoiceId) {
        AppointmentInvoice invoice = invoiceRepositoryPort.findInvoiceById(invoiceId);
        invoice.processCheckOut(paymentTimeout, clock);
        PaymentIntentResponse response = gateway.processPayment(invoice.getTotalAmount());
        Payment newLedger = Payment.paymentDeduction(
                invoice.getPatientId(),
                invoice.getInvoiceId(),
                invoice.getTotalAmount(),
                response.transactionId()
        );
        paymentRepositoryPort.save(newLedger);
        invoiceRepositoryPort.save(invoice);
        return PaymentInitializationResponse.from(invoice, newLedger, response.clientSecret());
    }
}

