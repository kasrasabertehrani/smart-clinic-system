package com.billingcontext.infrastructure.dto.response;

public record InvoiceStatusResponse(
        String invoiceId,
        String doctorId,
        String patientId,
        String appointmentId,
        String invoiceStatus) {

}
