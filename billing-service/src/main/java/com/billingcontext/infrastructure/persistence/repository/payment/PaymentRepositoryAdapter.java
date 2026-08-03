package com.billingcontext.infrastructure.persistence.repository.payment;

import com.billingcontext.application.repository.PaymentRepositoryPort;
import com.billingcontext.domain.invoice.InvoiceId;
import com.billingcontext.domain.payment.Payment;
import com.billingcontext.domain.payment.PaymentId;
import com.billingcontext.domain.payment.TransactionId;
import com.billingcontext.infrastructure.persistence.exception.DatabaseOperationException;
import com.billingcontext.infrastructure.persistence.exception.DuplicateResourceException;
import com.billingcontext.infrastructure.persistence.jpa.PaymentEntity;
import com.billingcontext.infrastructure.persistence.mapper.PaymentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PaymentRepositoryAdapter implements PaymentRepositoryPort {

    private final PaymentRepository jpaRepository;
    private final PaymentMapper mapper;

    @Override
    public Payment findPaymentById(PaymentId paymentId) {
        try {
            return jpaRepository.findById(paymentId.value())
                    .map(mapper::toPayment)
                    .orElseThrow(() -> new IllegalArgumentException("Payment not found with ID: " + paymentId.value()));
        } catch (DataAccessException e) {
            throw new DatabaseOperationException("Failed to retrieve payment data from the database.");
        }
    }

    @Override
    public void save(Payment payment) {
        try {
            PaymentEntity entity = mapper.toEntity(payment);
            jpaRepository.save(entity);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateResourceException("Failed to save payment: A conflicting record already exists.");
        } catch (DataAccessException e) {
            throw new DatabaseOperationException("An unexpected database error occurred while saving the payment.");
        }
    }

    @Override
    public Payment findPaymentByInvoiceId(InvoiceId invoiceId) {
        try {
            return jpaRepository.findSuccessfulPaymentByInvoiceId(invoiceId.value())
                    .map(mapper::toPayment)
                    .orElseThrow(() -> new IllegalArgumentException("Payment not found for Invoice ID: " + invoiceId.value()));
        } catch (DataAccessException e) {
            throw new DatabaseOperationException("Failed to retrieve payment data by invoice ID.");
        }
    }

    @Override
    public Payment findByTransactionId(TransactionId transactionId) {
        try {
            return jpaRepository.findByTransactionId(transactionId.value())
                    .map(mapper::toPayment)
                    .orElseThrow(() -> new IllegalArgumentException("Payment not found for Transaction ID: " + transactionId.value()));
        } catch (DataAccessException e) {
            throw new DatabaseOperationException("Failed to retrieve payment data by Transaction ID.");
        }
    }


    @Override
    public List<Payment> findPaymentsByInvoiceId(List<InvoiceId> invoiceIds) {
        try {
            List<String> rawIds = invoiceIds.stream()
                    .map(InvoiceId::value)
                    .toList();

            return jpaRepository.findByInvoiceIdIn(rawIds).stream()
                    .map(mapper::toPayment)
                    .toList();
        } catch (DataAccessException e) {
            throw new DatabaseOperationException("Failed to retrieve bulk payment data by invoice IDs.");
        }
    }

    @Override
    public void saveAll(List<Payment> payments) {
        try {
            List<PaymentEntity> entities = payments.stream()
                    .map(mapper::toEntity)
                    .toList();

            jpaRepository.saveAll(entities);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateResourceException("Failed to save payments in bulk: Conflicting records exist.");
        } catch (DataAccessException e) {
            throw new DatabaseOperationException("An unexpected database error occurred during bulk save of payments.");
        }
    }
}

