package com.billingcontext.infrastructure.persistence.repository.invoice;
import com.billingcontext.application.repository.InvoiceRepositoryPort;
import com.billingcontext.domain.invoice.AppointmentInvoice;
import com.billingcontext.domain.invoice.InvoiceStatus;

import com.billingcontext.domain.invoice.AppointmentId;
import com.billingcontext.domain.invoice.InvoiceId;
import com.billingcontext.infrastructure.persistence.exception.DatabaseOperationException;
import com.billingcontext.infrastructure.persistence.exception.DuplicateResourceException;
import com.billingcontext.infrastructure.persistence.jpa.InvoiceEntity;
import com.billingcontext.infrastructure.persistence.mapper.InvoiceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@RequiredArgsConstructor
public class InvoiceRepositoryAdapter implements InvoiceRepositoryPort {

    private final InvoiceRepository jpaRepository;
    private final InvoiceMapper mapper;

    @Override
    public void save(AppointmentInvoice appointmentInvoice) {
        try {
            InvoiceEntity entity = mapper.toEntity(appointmentInvoice);
            jpaRepository.save(entity);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateResourceException("Failed to save invoice: A conflicting record already exists.");
        } catch (DataAccessException e) {
            throw new DatabaseOperationException("An unexpected database error occurred while saving the invoice.");
        }
    }

    @Override
    public AppointmentInvoice findInvoiceById(InvoiceId invoiceId) {
        try {
            return jpaRepository.findById(invoiceId.value())
                    .map(mapper::toInvoice)
                    .orElseThrow(() -> new IllegalArgumentException("Invoice not found with ID: " + invoiceId.value()));
        } catch (DataAccessException e) {
            throw new DatabaseOperationException("Failed to retrieve invoice data from the database.");
        }
    }

    @Override
    public AppointmentInvoice findByAppointmentId(AppointmentId appointmentId) {
        try {
            return jpaRepository.findByAppointmentId(appointmentId.value())
                    .map(mapper::toInvoice)
                    .orElseThrow(() -> new IllegalArgumentException("Invoice not found for Appointment ID: " + appointmentId.value()));
        } catch (DataAccessException e) {
            throw new DatabaseOperationException("Failed to retrieve invoice data by appointment ID.");
        }
    }

    @Override
    public List<AppointmentInvoice> findDraftAndPendingInvoices() {
        try {
            List<InvoiceEntity> entities = jpaRepository.findByInvoiceStatusIn(
                    List.of(InvoiceStatus.DRAFT, InvoiceStatus.PAYMENT_PENDING)
            );

            return entities.stream()
                    .map(mapper::toInvoice)
                    .toList();
        } catch (DataAccessException e) {
            throw new DatabaseOperationException("Failed to retrieve pending and draft invoices.");
        }
    }

    @Override
    public void saveAll(List<AppointmentInvoice> appointmentInvoices) {
        try {

            List<InvoiceEntity> entities = appointmentInvoices.stream()
                    .map(mapper::toEntity)
                    .toList();


            jpaRepository.saveAll(entities);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateResourceException("Failed to save invoices in bulk: Conflicting records exist.");
        } catch (DataAccessException e) {
            throw new DatabaseOperationException("An unexpected database error occurred during bulk save.");
        }
    }
}