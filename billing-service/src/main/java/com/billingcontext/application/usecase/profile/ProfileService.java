package com.billingcontext.application.usecase.profile;

import com.billingcontext.application.repository.PricingProfileRepositoryPort;
import com.billingcontext.domain.profile.PricingProfile;
import com.billingcontext.infrastructure.dto.response.PricingProfileResponse;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Transactional
public class ProfileService implements ProfileUseCase {
    private final PricingProfileRepositoryPort profileRepository;

    @Override
    public PricingProfileResponse createProfile(CreateProfileCommand command) {
        PricingProfile newProfile = PricingProfile.openUpPricingProfile(command.doctorId(), command.hourlyRate());
        profileRepository.save(newProfile);
        return PricingProfileResponse.from(newProfile);
    }

    @Override
    public PricingProfileResponse updateProfile(UpdateProfileCommand command) {
        PricingProfile existingProfile = profileRepository.findByDoctorId(command.doctorId());
        existingProfile.changeHourlyRate(command.hourlyRate());
        profileRepository.save(existingProfile);
        return PricingProfileResponse.from(existingProfile);
    }
}
