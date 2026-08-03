package com.billingcontext.infrastructure.persistence.repository.profile;

import com.billingcontext.application.repository.PricingProfileRepositoryPort;
import com.billingcontext.domain.profile.PricingProfile;
import com.billingcontext.domain.shared.id.DoctorId;
import com.billingcontext.infrastructure.persistence.exception.DatabaseOperationException;
import com.billingcontext.infrastructure.persistence.exception.DuplicateResourceException;
import com.billingcontext.infrastructure.persistence.jpa.PricingProfileEntity;
import com.billingcontext.infrastructure.persistence.mapper.PricingProfileMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class PricingProfileRepositoryAdapter implements PricingProfileRepositoryPort {

    private final PricingProfileRepository jpaRepository;
    private final PricingProfileMapper mapper;

    @Override
    public PricingProfile findByDoctorId(DoctorId doctorId) {
        try {
            return jpaRepository.findByDoctorId(doctorId.value())
                    .map(mapper::toProfile)
                    .orElseThrow(() -> new IllegalArgumentException("Pricing Profile not found for Doctor ID: " + doctorId.value()));
        } catch (DataAccessException e) {
            throw new DatabaseOperationException("Failed to retrieve pricing profile data from the database.");
        }
    }

    @Override
    public void save(PricingProfile pricingProfile) {
        try {
            PricingProfileEntity entity = mapper.toEntity(pricingProfile);
            jpaRepository.save(entity);
        } catch (DataIntegrityViolationException e) {

            throw new DuplicateResourceException("Failed to save pricing profile: A conflicting record already exists for this doctor.");
        } catch (DataAccessException e) {
            throw new DatabaseOperationException("An unexpected database error occurred while saving the pricing profile.");
        }
    }
}
