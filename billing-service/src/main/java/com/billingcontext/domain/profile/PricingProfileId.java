package com.billingcontext.domain.profile;

public record PricingProfileId(String value) {
    public PricingProfileId {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Patient ID cannot be null or blank");
        }
    }
}