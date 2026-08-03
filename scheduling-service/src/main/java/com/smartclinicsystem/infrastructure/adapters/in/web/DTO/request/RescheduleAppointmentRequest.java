package com.smartclinicsystem.infrastructure.adapters.in.web.DTO.request;

import com.smartclinicsystem.domain.Appointment;

import com.smartclinicsystem.infrastructure.adapters.in.web.validation.ValidSharpTime;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;



import java.time.LocalDate;
import java.time.LocalTime;

@Getter
public class RescheduleAppointmentRequest {

    @NotNull(message = "date is required")
    @Future(message = "date must be in the future")
    private LocalDate appointmentDate;

    @NotNull(message = "start time is required")
    @ValidSharpTime
    private LocalTime startTime;

    @NotNull(message = "Cancel Initiator cannot be null")
    private Appointment.CancellationInitiator cancelInitiator;




}
