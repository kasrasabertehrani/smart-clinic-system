package com.billingcontext.application.repository;

import com.billingcontext.domain.invoice.AppointmentId;
import com.billingcontext.domain.invoice.AppointmentInvoice;
import com.billingcontext.domain.invoice.InvoiceId;

import java.util.List;

public interface InvoiceRepositoryPort {
    void save(AppointmentInvoice appointmentInvoice);
    AppointmentInvoice findInvoiceById(InvoiceId invoiceId);
    List<AppointmentInvoice> findDraftAndPendingInvoices();
    AppointmentInvoice findByAppointmentId(AppointmentId appointmentId);
    void saveAll(List<AppointmentInvoice> appointmentInvoices);

}
