package com.smartclinicsystem.infrastructure.adapters.in.DTO.response;

import com.smartclinicsystem.domain.Appointment;

import java.time.LocalDate;
import java.time.LocalTime;

public record RescheduleAppointmentResponse(String appointmentId,
                                            String doctorId,
                                            String patientId,
                                            LocalDate appointmentDate,
                                            LocalTime startTime,
                                            LocalTime endTime,
                                            String appointmentStatus,
                                            String rescheduledId) {

    public static RescheduleAppointmentResponse from(Appointment appointment) {
        return new RescheduleAppointmentResponse(
                appointment.getId().value(),
                appointment.getDoctorId().value(),
                appointment.getPatientId().value(),
                appointment.getAppointmentTimeSlot().date(),
                appointment.getAppointmentTimeSlot().start().time(),
                appointment.getAppointmentTimeSlot().getEnd().time(),
                appointment.getAppointmentStatus().name(),
                appointment.getRescheduledFromId().value()
        );
    }
}
