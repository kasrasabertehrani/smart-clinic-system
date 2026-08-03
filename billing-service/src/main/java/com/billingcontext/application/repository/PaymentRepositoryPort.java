package com.billingcontext.application.repository;


import com.billingcontext.domain.invoice.InvoiceId;
import com.billingcontext.domain.payment.Payment;
import com.billingcontext.domain.payment.PaymentId;
import com.billingcontext.domain.payment.TransactionId;

import java.util.List;


public interface PaymentRepositoryPort {
    Payment findPaymentById(PaymentId paymentId);
    void save(Payment payment);
    Payment findPaymentByInvoiceId(InvoiceId invoiceId);
    Payment findByTransactionId(TransactionId transactionId);
    List<Payment> findPaymentsByInvoiceId(List<InvoiceId> invoiceIds);
    void saveAll(List<Payment> payments);
}
