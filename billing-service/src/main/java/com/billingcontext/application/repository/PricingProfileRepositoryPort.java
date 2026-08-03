package com.billingcontext.application.repository;

import com.billingcontext.domain.profile.PricingProfile;
import com.billingcontext.domain.profile.PricingProfileId;
import com.billingcontext.domain.shared.id.DoctorId;

public interface PricingProfileRepositoryPort {
    PricingProfile findByDoctorId(DoctorId doctorId);
    void save(PricingProfile pricingProfile);
}
