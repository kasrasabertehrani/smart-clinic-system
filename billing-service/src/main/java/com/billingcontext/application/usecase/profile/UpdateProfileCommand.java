package com.billingcontext.application.usecase.profile;

import com.billingcontext.domain.shared.finance.Money;
import com.billingcontext.domain.shared.id.DoctorId;

public record UpdateProfileCommand(DoctorId doctorId, Money hourlyRate) {
}
