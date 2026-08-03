package com.billingcontext.infrastructure.persistence.repository.profile;

import com.billingcontext.infrastructure.persistence.jpa.PricingProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PricingProfileRepository extends JpaRepository<PricingProfileEntity, String> {
    Optional<PricingProfileEntity> findByDoctorId(String doctorId);
}
