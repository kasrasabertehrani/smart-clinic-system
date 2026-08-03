package com.smartclinicsystem.domain.events;

import com.smartclinicsystem.domain.Appointment;
import com.smartclinicsystem.domain.vo.AppointmentId;
import com.smartclinicsystem.domain.vo.TimeSlot;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public record AppointmentWasCanceled(
        String appointmentId,
        String cancelInitiator,
        Instant bookingTime,
        Instant cancellationTime,
        Instant appointmentTime,
        Instant occurredOn
) implements DomainEvent {

    private static final ZoneId CLINIC_ZONE = ZoneId.of("Europe/Rome");

    public AppointmentWasCanceled(
            AppointmentId appointmentId,
            Appointment.CancellationInitiator cancellationInitiator,
            LocalDateTime createdAt,
            LocalDateTime updatedAt,
            TimeSlot timeSlot) {

        this(
                appointmentId.value(),
                cancellationInitiator.name(),

                createdAt.atZone(CLINIC_ZONE).toInstant(),
                updatedAt.atZone(CLINIC_ZONE).toInstant(),

                LocalDateTime.of(timeSlot.date(), timeSlot.getStartDateTime().toLocalTime())
                        .atZone(CLINIC_ZONE).toInstant(),

                Instant.now()
        );
    }
}
