package com.smartclinicsystem.infrastructure.adapters.in.DTO.response;

import com.smartclinicsystem.domain.Appointment;

import java.time.LocalDate;
import java.time.LocalTime;

public record CancelAppointmentResponse(String appointmentId, String doctorId, String patientId,
                                        LocalDate appointmentDate,
                                        LocalTime startTime,
                                        LocalTime endTime,
                                        String appointmentStatus,
                                        String cancelInitiator) {

    public static CancelAppointmentResponse from(Appointment newAppointment) {
        return new CancelAppointmentResponse(
                newAppointment.getId().value(),
                newAppointment.getDoctorId().value(),
                newAppointment.getPatientId().value(),
                newAppointment.getAppointmentTimeSlot().date(),
                newAppointment.getAppointmentTimeSlot().start().time(),
                newAppointment.getAppointmentTimeSlot().getEnd().time(),
                newAppointment.getAppointmentStatus().name(),
                newAppointment.getCancelledBy().name()
        );
    }

}
