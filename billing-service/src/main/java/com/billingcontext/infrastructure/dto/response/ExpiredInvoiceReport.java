package com.billingcontext.infrastructure.dto.response;

import java.time.LocalDateTime;

public record ExpiredInvoiceReport(
        String invoiceId,
        String patientId,
        LocalDateTime expirationDate,
        LocalDateTime timeOfExpiration
) {}
