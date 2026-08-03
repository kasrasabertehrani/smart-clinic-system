package com.billingcontext.infrastructure.persistence.jpa;

import jakarta.persistence.*;
import lombok.Getter;


import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "profile")
public class PricingProfileEntity {

    @Id
    String id;

    @Column(name = "doctor_id", nullable = false, unique = true)
    String doctorId;

    @Column(name = "base_currency", nullable = false)
    String currency;

    @Column(name = "hourly_rate", nullable = false, precision = 19, scale = 4)
    BigDecimal hourlyRate;

    @Column(name = "created_at", updatable = false)
    LocalDateTime createdAt;

    @Column(name = "updated_at")
    LocalDateTime updatedAt;

    public PricingProfileEntity(String id, String doctorId, String currency, BigDecimal hourlyRate, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.doctorId = doctorId;
        this.currency = currency;
        this.hourlyRate = hourlyRate;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    public PricingProfileEntity() {
    }

}
