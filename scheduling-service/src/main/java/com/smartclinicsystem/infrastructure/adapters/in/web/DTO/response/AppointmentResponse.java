package com.smartclinicsystem.infrastructure.adapters.in.web.DTO.response;

import com.smartclinicsystem.domain.Appointment;

import java.time.LocalDate;
import java.time.LocalTime;

public record AppointmentResponse(String appointmentId,
        String doctorId,
        String patientId,
        LocalDate appointmentDate,
        LocalTime startTime,
        LocalTime endTime,
        String appointmentStatus) {


    public static AppointmentResponse from(Appointment appointment) {
        return new AppointmentResponse(
                appointment.getId().value(),
                appointment.getDoctorId().value(),
                appointment.getPatientId().value(),
                appointment.getAppointmentTimeSlot().date(),
                appointment.getAppointmentTimeSlot().start().time(),
                appointment.getAppointmentTimeSlot().getEnd().time(),
                appointment.getAppointmentStatus().name()
        );
    }


}
