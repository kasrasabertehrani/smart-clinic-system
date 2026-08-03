package com.billingcontext.infrastructure.persistence.mapper;

import com.billingcontext.domain.profile.PricingProfile;
import com.billingcontext.domain.profile.PricingProfileId;
import com.billingcontext.domain.shared.finance.Money;
import com.billingcontext.domain.shared.id.DoctorId;
import com.billingcontext.infrastructure.persistence.jpa.PricingProfileEntity;
import org.springframework.stereotype.Component;

@Component
public class PricingProfileMapper {

    public PricingProfileEntity toEntity(PricingProfile pricingProfile) {
        if (pricingProfile == null) {
            return null;
        }
        return new PricingProfileEntity(
                pricingProfile.getPricingProfileId().value(),
                pricingProfile.getDoctorId().value(),
                pricingProfile.getHourlyRate().currency().toString(),
                pricingProfile.getHourlyRate().amount(),
                pricingProfile.getCreatedAt(),
                pricingProfile.getUpdatedAt()
        );
    }
    public PricingProfile toProfile(PricingProfileEntity pricingProfileEntity) {
        if (pricingProfileEntity == null) {
            return null;
        }
        return new PricingProfile(
                new PricingProfileId(pricingProfileEntity.getId()),
                new DoctorId(pricingProfileEntity.getDoctorId()),
                Money.of(pricingProfileEntity.getHourlyRate(), pricingProfileEntity.getCurrency()),
                pricingProfileEntity.getCreatedAt(),
                pricingProfileEntity.getUpdatedAt()
        );
    }
}
