package com.smartclinicsystem.application.port.in;

import com.smartclinicsystem.infrastructure.adapters.in.web.DTO.request.AddAppointmentCommand;
import com.smartclinicsystem.infrastructure.adapters.in.web.DTO.request.CancelAppointmentCommand;
import com.smartclinicsystem.infrastructure.adapters.in.web.DTO.request.RescheduleAppointmentCommand;
import com.smartclinicsystem.infrastructure.adapters.in.web.DTO.request.RescheduleSystemCanceledCommand;
import com.smartclinicsystem.infrastructure.adapters.in.web.DTO.response.AppointmentResponse;
import com.smartclinicsystem.infrastructure.adapters.in.web.DTO.response.CancelAppointmentResponse;
import com.smartclinicsystem.infrastructure.adapters.in.web.DTO.response.RescheduleAppointmentResponse;

public interface SchedulingUseCase {
    AppointmentResponse addAppointment(AddAppointmentCommand command);

    CancelAppointmentResponse cancelAppointment(CancelAppointmentCommand command, String appointment_Id);

    AppointmentResponse checkInPatient(String appointment_id);

    AppointmentResponse completeAppointment(String appointmentId);

    AppointmentResponse markAsNoShow(String appointmentId);

    RescheduleAppointmentResponse rescheduleActiveAppointment(RescheduleAppointmentCommand command, String appointmentId);

    RescheduleAppointmentResponse rescheduleSystemCanceledAppointment(RescheduleSystemCanceledCommand command,
                                                                      String appointmentId);

}
