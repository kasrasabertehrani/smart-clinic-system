package com.billingcontext.application.usecase.profile;


import com.billingcontext.infrastructure.dto.response.PricingProfileResponse;

public interface ProfileUseCase {
    PricingProfileResponse createProfile(CreateProfileCommand command);
    PricingProfileResponse updateProfile(UpdateProfileCommand command);
}
