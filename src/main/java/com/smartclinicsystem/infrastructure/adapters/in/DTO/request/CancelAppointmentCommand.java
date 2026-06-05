package com.smartclinicsystem.infrastructure.adapters.in.DTO.request;

import com.smartclinicsystem.domain.Appointment;
import jakarta.validation.constraints.NotNull;


public class CancelAppointmentCommand {

    @NotNull(message = "Cancel Initiator cannot be null")
    private Appointment.CancellationInitiator cancelInitiator;

    public Appointment.CancellationInitiator getCancelInitiator() {
        return cancelInitiator;
    }


}
