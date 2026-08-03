package com.billingcontext.infrastructure.messaging.appointment.booking;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;

@JsonIgnoreProperties(ignoreUnknown = true)
public record AppointmentBookedEvent(
        @NotNull
        String appointmentId,
        @NotNull
        String doctorId,
        @NotNull
        String patientId,
        @Future
        Instant appointmentTime,
        @NotNull
        @Min(15)
        int duration) {
}
