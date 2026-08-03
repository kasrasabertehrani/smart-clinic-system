package com.smartclinicsystem.infrastructure.adapters.in.web.DTO.request;


import com.smartclinicsystem.infrastructure.adapters.in.web.validation.ValidSharpTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;


public class ChangeScheduleRequest {

    @NotNull(message = "Effective date is required")
    @FutureOrPresent(message = "Effective date must be today or in the future")
    private LocalDate validFrom;


    @NotEmpty(message = "Shifts mapping cannot be empty")
    private Map<DayOfWeek, @Valid List<@Valid ShiftDTO>> shifts;

    public static class ShiftDTO {
        @NotNull(message = "Start time is required")
        @ValidSharpTime
        private LocalTime startTime;

        @NotNull(message = "End time is required")
        @ValidSharpTime
        private LocalTime endTime;

        public LocalTime getStartTime() { return startTime; }
        public LocalTime getEndTime() { return endTime; }
    }

    public LocalDate getValidFrom() { return validFrom; }
    public Map<DayOfWeek, List<ShiftDTO>> getShifts() { return shifts; }

}