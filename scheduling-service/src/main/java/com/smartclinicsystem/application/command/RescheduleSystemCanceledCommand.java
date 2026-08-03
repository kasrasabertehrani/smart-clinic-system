package com.smartclinicsystem.application.command;

import com.smartclinicsystem.domain.vo.AppointmentId;
import com.smartclinicsystem.domain.vo.TimeSlot;

public record RescheduleSystemCanceledCommand(
        AppointmentId appointmentId, TimeSlot newTimeSlot
) {
}
