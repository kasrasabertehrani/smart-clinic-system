package com.smartclinicsystem.application.port.in;


import com.smartclinicsystem.application.command.AddAppointmentCommand;
import com.smartclinicsystem.application.command.CancelAppointmentCommand;
import com.smartclinicsystem.application.command.RescheduleAppointmentCommand;
import com.smartclinicsystem.application.command.RescheduleSystemCanceledCommand;
import com.smartclinicsystem.domain.vo.AppointmentId;
import com.smartclinicsystem.infrastructure.adapters.in.web.DTO.response.AppointmentResponse;
import com.smartclinicsystem.infrastructure.adapters.in.web.DTO.response.CancelAppointmentResponse;
import com.smartclinicsystem.infrastructure.adapters.in.web.DTO.response.RescheduleAppointmentResponse;

public interface SchedulingUseCase {
    AppointmentResponse addAppointment(AddAppointmentCommand command);

    CancelAppointmentResponse cancelAppointment(CancelAppointmentCommand command);

    AppointmentResponse checkInPatient(AppointmentId appointmentId);

    AppointmentResponse completeAppointment(AppointmentId appointmentId);

    AppointmentResponse markAsNoShow(AppointmentId appointmentId);

    RescheduleAppointmentResponse rescheduleActiveAppointment(RescheduleAppointmentCommand command);

    RescheduleAppointmentResponse rescheduleSystemCanceledAppointment(RescheduleSystemCanceledCommand command);

}
