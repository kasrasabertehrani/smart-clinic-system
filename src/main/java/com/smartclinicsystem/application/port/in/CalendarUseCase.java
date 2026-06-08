package com.smartclinicsystem.application.port.in;

import com.smartclinicsystem.infrastructure.adapters.in.web.DTO.request.AddUnavailabilityCommand;
import com.smartclinicsystem.infrastructure.adapters.in.web.DTO.request.ChangeScheduleCommand;
import com.smartclinicsystem.infrastructure.adapters.in.web.DTO.response.AddUnavailabilityResponse;
import com.smartclinicsystem.infrastructure.adapters.in.web.DTO.response.ChangeScheduleResponse;

public interface CalendarUseCase {

    AddUnavailabilityResponse addUnavailability(AddUnavailabilityCommand command, String doctor_id);

    ChangeScheduleResponse changeSchedule(ChangeScheduleCommand command, String doctor_id);
}
