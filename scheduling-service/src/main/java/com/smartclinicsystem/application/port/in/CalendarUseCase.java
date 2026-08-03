package com.smartclinicsystem.application.port.in;

import com.smartclinicsystem.application.command.AddUnavailabilityCommand;
import com.smartclinicsystem.application.command.ChangeScheduleCommand;
import com.smartclinicsystem.infrastructure.adapters.in.web.DTO.response.AddUnavailabilityResponse;
import com.smartclinicsystem.infrastructure.adapters.in.web.DTO.response.ChangeScheduleResponse;

public interface CalendarUseCase {

    AddUnavailabilityResponse addUnavailability(AddUnavailabilityCommand command);

    ChangeScheduleResponse changeSchedule(ChangeScheduleCommand command);
}
