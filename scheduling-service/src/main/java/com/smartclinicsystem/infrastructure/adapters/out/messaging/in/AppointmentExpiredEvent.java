package com.smartclinicsystem.infrastructure.adapters.out.messaging.in;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@JsonIgnoreProperties(ignoreUnknown = true)
public record AppointmentExpiredEvent(
        @NotNull(message = "Appointment ID cannot be null")
        @NotBlank(message = "Appointment ID cannot be blank")
        String appointmentId) {

}
