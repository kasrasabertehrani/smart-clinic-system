package com.billingcontext.infrastructure.messaging.appointment.cancellation;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.PastOrPresent;

import java.time.Instant;

@JsonIgnoreProperties(ignoreUnknown = true)
public record AppointmentCancelledEvent(
        @NotNull(message = "Appointment ID cannot be null")
        String appointmentId,

        @NotNull(message = "Cancel initiator cannot be null")
        String cancelInitiator,

        @Past(message = "Booking time must be in the past")
        Instant bookingTime,

        @PastOrPresent(message = "Cancellation time must be in the past or present")
        Instant cancellationTime,

        @Future(message = "Appointment time must be in the future")
        Instant appointmentTime) {
}
