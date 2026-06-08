package com.smartclinicsystem.infrastructure.adapters.in.web.DTO.request;

import com.smartclinicsystem.domain.Appointment;
import com.smartclinicsystem.domain.vo.SharpTime;
import com.smartclinicsystem.domain.vo.TimeSlot;
import com.smartclinicsystem.infrastructure.adapters.in.web.validation.ValidSharpTime;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

public class RescheduleSystemCanceledCommand {
    @NotNull(message = "date is required")
    @Future(message = "date must be in the future")
    private LocalDate appointmentDate;

    @NotNull(message = "start time is required")
    @ValidSharpTime
    private LocalTime startTime;

    @NotNull(message = "Cancel Initiator cannot be null")
    private Appointment.CancellationInitiator cancelInitiator;

    public TimeSlot createTimeSlot(){
        SharpTime sharpTime = new SharpTime(startTime);
        return new TimeSlot(appointmentDate, sharpTime, Duration.ofMinutes(60));
    }
    public Appointment.CancellationInitiator getCancelInitiator() {
        return cancelInitiator;
    }
}
