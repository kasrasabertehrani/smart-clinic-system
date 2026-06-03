package com.smartclinicsystem.infrastructure.adapters.in;



import com.smartclinicsystem.domain.vo.DoctorId;
import com.smartclinicsystem.domain.vo.PatientId;
import com.smartclinicsystem.domain.vo.SharpTime;
import com.smartclinicsystem.domain.vo.TimeSlot;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

public class AddAppointmentCommand {

    @NotNull(message = "Doctor ID cannot be null")
    @NotBlank(message = "Doctor ID cannot be blank")
    private String doctorId;

    @NotNull(message = "Doctor ID cannot be null")
    @NotBlank(message = "Doctor ID cannot be blank")
    private String patientId;

    @NotNull(message = "date is required")
    @Future(message = "date must be in the future")
    private LocalDate appointmentDate;

    @NotNull(message = "start time is required")
    private LocalTime startTime;

    public PatientId getPatientId() {
        return new PatientId(patientId);
    }
    public DoctorId getDoctorId() {
        return new DoctorId(doctorId);
    }
    public TimeSlot createTimeSlot(){
        SharpTime sharpTime = new SharpTime(startTime);
        return new TimeSlot(appointmentDate, sharpTime, Duration.ofMinutes(60));
    }
}
