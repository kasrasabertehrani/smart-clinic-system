package com.billingcontext.infrastructure.controller.command.profile;

import com.billingcontext.application.usecase.profile.CreateProfileCommand;
import com.billingcontext.application.usecase.profile.UpdateProfileCommand;
import com.billingcontext.domain.shared.finance.Money;
import com.billingcontext.domain.shared.id.DoctorId;
import com.billingcontext.infrastructure.dto.request.PricingProfileRequest;
import org.springframework.stereotype.Component;

@Component
public class ProfileRequestTranslator {

    public CreateProfileCommand translateCreate(PricingProfileRequest request) {
        return new CreateProfileCommand(
                new DoctorId(request.doctorId()),
                Money.of(request.hourlyRate(), request.currency())
        );
    }

    public UpdateProfileCommand translateUpdate(PricingProfileRequest request) {
        return new UpdateProfileCommand(
                new DoctorId(request.doctorId()),
                Money.of(request.hourlyRate(), request.currency())
        );
    }
}
