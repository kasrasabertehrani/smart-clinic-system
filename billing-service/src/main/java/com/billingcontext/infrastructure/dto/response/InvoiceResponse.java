package com.billingcontext.infrastructure.dto.response;



import com.billingcontext.domain.invoice.AppointmentInvoice;

import java.time.LocalDateTime;

public record InvoiceResponse(
        String invoiceId,
        String appointmentId,
        String doctorId,
        String patientId,
        LocalDateTime expirationDate,
        String discountPercentage,
        String basePrice,
        String currency,
        String totalAmount,
        String refundAmount,
        String invoiceStatus
        ) {
    public static InvoiceResponse from(AppointmentInvoice invoice) {
        return new InvoiceResponse(
                invoice.getInvoiceId().value(),
                invoice.getAppointmentId().value(),
                invoice.getDoctorId().value(),
                invoice.getPatientId().value(),
                invoice.getExpirationDate(),
                invoice.getDiscountPercentage().toString(),
                invoice.getBasePrice().amount().toString(),
                invoice.getBasePrice().currency().toString(),
                invoice.getTotalAmount().amount().toString(),
                invoice.getToBeRefunded().amount().toString(),
                invoice.getInvoiceStatus().name()
        );
    }
}
