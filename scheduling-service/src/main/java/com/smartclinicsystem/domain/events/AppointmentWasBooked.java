package com.smartclinicsystem.domain.events;

import com.smartclinicsystem.domain.vo.AppointmentId;
import com.smartclinicsystem.domain.vo.DoctorId;
import com.smartclinicsystem.domain.vo.PatientId;
import com.smartclinicsystem.domain.vo.TimeSlot;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public record AppointmentWasBooked(String appointmentId,
                                   String doctorId,
                                   String patientId,
                                   Instant appointmentTime,
                                   int duration,
                                   Instant occurredOn) implements DomainEvent {


    private static final ZoneId CLINIC_ZONE = ZoneId.of("Europe/Rome");


    public AppointmentWasBooked(
            AppointmentId appointmentId,
            DoctorId doctorId,
            PatientId patientId,
            TimeSlot timeSlot) {

        this(
                appointmentId.value(),
                doctorId.value(),
                patientId.value(),
                convertTimeSlotToInstant(timeSlot),
                (int) timeSlot.duration().toMinutes(),
                Instant.now()
        );
    }


    private static Instant convertTimeSlotToInstant(TimeSlot timeSlot) {
        return LocalDateTime.of(timeSlot.date(), timeSlot.getStartDateTime().toLocalTime())
                .atZone(CLINIC_ZONE)
                .toInstant();
    }

}
