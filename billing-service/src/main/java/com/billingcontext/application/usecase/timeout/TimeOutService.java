package com.billingcontext.application.usecase.timeout;

import com.billingcontext.application.DomainEventPublisher;
import com.billingcontext.application.repository.InvoiceRepositoryPort;
import com.billingcontext.application.repository.PaymentRepositoryPort;
import com.billingcontext.domain.invoice.AppointmentInvoice;
import com.billingcontext.domain.invoice.InvoiceId;
import com.billingcontext.domain.payment.Payment;


import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional
public class TimeOutService implements TimeOutUseCase {
    private final InvoiceRepositoryPort invoiceRepository;
    private final PaymentRepositoryPort paymentRepository;
    private final Clock clock;
    private final DomainEventPublisher domainEventPublisher;

    @Override
    public void handelPaymentTimeOut() {
        List<AppointmentInvoice> invoices = invoiceRepository.findDraftAndPendingInvoices();

        List<AppointmentInvoice> timedOutInvoices = invoices.stream()
                .map(invoice -> invoice.evaluateTimeouts(clock))
                .flatMap(Optional::stream)
                .toList();

        List<InvoiceId> timedOutInvoiceIds = timedOutInvoices.stream()
                .map(AppointmentInvoice::getInvoiceId)
                .toList();

        List<Payment> allPaymentsForInvoices = paymentRepository.findPaymentsByInvoiceId(timedOutInvoiceIds);
        List<Payment> paymentsToExpire = allPaymentsForInvoices.stream()
                .filter(Payment::isPending)
                .toList();

        paymentsToExpire.forEach(Payment::expirePayment);

        paymentRepository.saveAll(paymentsToExpire);
        invoiceRepository.saveAll(timedOutInvoices);

        timedOutInvoices.stream()
                .filter(AppointmentInvoice::isExpired)
                .forEach(invoice -> {
                    invoice.getDomainEvents().forEach(domainEventPublisher::publish);
                    invoice.clearDomainEvents();
                });
    }

}
