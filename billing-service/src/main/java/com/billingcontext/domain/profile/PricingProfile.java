package com.billingcontext.domain.profile;

import com.billingcontext.domain.shared.id.DoctorId;
import com.billingcontext.domain.shared.finance.Money;
import lombok.Getter;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class PricingProfile {
    private final PricingProfileId pricingProfileId;
    private final DoctorId doctorId;
    private Money HourlyRate;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public PricingProfile(PricingProfileId pricingProfileId, DoctorId doctorId, Money HourlyRate,
                           LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.pricingProfileId = pricingProfileId;
        this.doctorId = doctorId;
        this.HourlyRate = HourlyRate;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    public static PricingProfile openUpPricingProfile(DoctorId doctorId, Money HourlyRate) {
        if(HourlyRate.isZero()) {
            throw new PricingProfileException("Hourly Rate cannot be zero");
        }
        return new PricingProfile(
                new PricingProfileId(UUID.randomUUID().toString()),
                doctorId,
                HourlyRate,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    public void changeHourlyRate(Money HourlyRate) {
        if(HourlyRate.isZero()) {
            throw new PricingProfileException("Hourly Rate cannot be zero");
        }
        this.HourlyRate = HourlyRate;
        this.updatedAt = LocalDateTime.now();
    }
    public Money calculateAppointmentFee(BigDecimal hours) {

        if (hours.compareTo(BigDecimal.ZERO) <= 0) {
            throw new PricingProfileException("Hours must be greater than zero");
        }
        return this.HourlyRate.multiply(hours);
    }



}
