package com.smartclinicsystem.application.command;

import com.smartclinicsystem.domain.Appointment;
import com.smartclinicsystem.domain.vo.AppointmentId;

public record CancelAppointmentCommand(AppointmentId appointmentId, Appointment.CancellationInitiator cancelInitiator) {
}
