package com.billingcontext.infrastructure.persistence.mapper;

import com.billingcontext.domain.invoice.AppointmentId;
import com.billingcontext.domain.invoice.AppointmentInvoice;
import com.billingcontext.domain.invoice.InvoiceId;
import com.billingcontext.domain.shared.finance.Money;
import com.billingcontext.domain.shared.finance.Percentage;
import com.billingcontext.domain.shared.id.DoctorId;
import com.billingcontext.domain.shared.id.PatientId;
import com.billingcontext.infrastructure.persistence.jpa.InvoiceEntity;
import org.springframework.stereotype.Component;


@Component
public class InvoiceMapper {

    public InvoiceEntity toEntity(AppointmentInvoice invoice) {

        if (invoice == null) return null;

        return new InvoiceEntity(
                invoice.getInvoiceId().value(),
                invoice.getAppointmentId().value(),
                invoice.getDoctorId().value(),
                invoice.getPatientId().value(),
                invoice.getExpirationDate(),
                invoice.getPaymentDurationWindow(),
                invoice.getBasePrice().currency().toString(),
                invoice.getBasePrice().amount(),
                invoice.getDiscountPercentage().value(),
                invoice.getTotalAmount().amount(),
                invoice.getToBeRefunded().amount(),
                invoice.getCreatedAt(),
                invoice.getUpdatedAt(),
                invoice.getInvoiceStatus()
        );
    }

    public AppointmentInvoice toInvoice(InvoiceEntity entity) {

        if (entity == null) return null;

        return new AppointmentInvoice(
                new InvoiceId(entity.getInvoiceId()),
                new AppointmentId(entity.getAppointmentId()),
                new DoctorId(entity.getDoctorId()),
                new PatientId(entity.getPatientId()),
                entity.getExpirationDate(),
                entity.getPaymentDurationWindow(),
                Money.of(entity.getBasePrice(), entity.getCurrency()),
                new Percentage (entity.getDiscountPercentage()),
                Money.of(entity.getTotalPrice(), entity.getCurrency()),
                Money.of(entity.getToBeRefunded(), entity.getCurrency()),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getInvoiceStatus()
        );
    }

}
