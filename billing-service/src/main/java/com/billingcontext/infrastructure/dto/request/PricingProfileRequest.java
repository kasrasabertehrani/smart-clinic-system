package com.billingcontext.infrastructure.dto.request;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record PricingProfileRequest(
        @NotNull(message = "Doctor ID cannot be null")
        String doctorId,
        @NotNull(message = "Currency cannot be null")
        String currency,
        @NotNull(message = "Hourly rate cannot be null")
        BigDecimal hourlyRate
) {
}
