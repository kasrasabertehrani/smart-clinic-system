package com.smartclinicsystem.infrastructure.adapters.in;

import com.smartclinicsystem.domain.vo.TimePeriod;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class AddUnavailabilityCommand {

    @NotNull(message = "start time is required")
    @Future(message = "start time must be in the future")
    private LocalDateTime startTime;

    @NotNull(message = "end time is required")
    @Future(message = "end time must be in the future")
    private LocalDateTime endTime;

    public TimePeriod createTimePeriod() {
        return new TimePeriod(startTime, endTime);
    }
}
