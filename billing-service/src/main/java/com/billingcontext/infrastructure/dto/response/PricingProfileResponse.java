package com.billingcontext.infrastructure.dto.response;

import com.billingcontext.domain.profile.PricingProfile;

public record PricingProfileResponse(
        String doctorId,
        String HourlyRate,
        String currency) {

    public static PricingProfileResponse from(PricingProfile pricingProfile) {
        return new PricingProfileResponse(
                pricingProfile.getDoctorId().value(),
                pricingProfile.getHourlyRate().amount().toString(),
                pricingProfile.getHourlyRate().currency().toString()
        );
    }
}
