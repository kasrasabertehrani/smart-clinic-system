package com.smartclinicsystem.application.command;

import com.smartclinicsystem.domain.Appointment;
import com.smartclinicsystem.domain.vo.AppointmentId;
import com.smartclinicsystem.domain.vo.TimeSlot;

public record RescheduleAppointmentCommand(AppointmentId appointmentId, TimeSlot newTimeSlot, Appointment.CancellationInitiator cancelInitiator) {
}
