package com.smartclinicsystem.infrastructure.adapters.in.web.DTO.request;

import com.smartclinicsystem.infrastructure.adapters.in.web.validation.ValidSharpTime;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;


import java.time.LocalDate;
import java.time.LocalTime;

@Getter
public class AddAppointmentRequest {

    @NotNull(message = "Doctor ID cannot be null")
    @NotBlank(message = "Doctor ID cannot be blank")
    private String doctorId;

    @NotNull(message = "Patient ID cannot be null")
    @NotBlank(message = "Patient ID cannot be blank")
    private String patientId;

    @NotNull(message = "date is required")
    @FutureOrPresent(message = "date must be in the future")
    @Getter
    private LocalDate appointmentDate;

    @NotNull(message = "start time is required")
    @ValidSharpTime
    @Getter
    private LocalTime startTime;


}

