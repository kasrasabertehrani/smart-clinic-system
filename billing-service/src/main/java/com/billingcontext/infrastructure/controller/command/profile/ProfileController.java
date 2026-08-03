package com.billingcontext.infrastructure.controller.command.profile;

import com.billingcontext.application.usecase.profile.CreateProfileCommand;
import com.billingcontext.application.usecase.profile.ProfileUseCase;
import com.billingcontext.application.usecase.profile.UpdateProfileCommand;
import com.billingcontext.infrastructure.dto.request.PricingProfileRequest;
import com.billingcontext.infrastructure.dto.response.PricingProfileResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/profile")
public class ProfileController {
    private final ProfileUseCase profileUseCase;
    private final ProfileRequestTranslator translator;

    @PostMapping("/create")
    public ResponseEntity<PricingProfileResponse> createProfile(@Valid @RequestBody PricingProfileRequest request) {

        CreateProfileCommand command = translator.translateCreate(request);
        PricingProfileResponse response = profileUseCase.createProfile(command);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/update")
    public ResponseEntity<PricingProfileResponse> updateProfile(@Valid @RequestBody PricingProfileRequest request) {

        UpdateProfileCommand command = translator.translateUpdate(request);
        PricingProfileResponse response = profileUseCase.updateProfile(command);
        return ResponseEntity.ok(response);
    }



}
