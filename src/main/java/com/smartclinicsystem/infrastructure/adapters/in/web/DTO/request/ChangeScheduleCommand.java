package com.smartclinicsystem.infrastructure.adapters.in.web.DTO.request;

import com.smartclinicsystem.domain.vo.SharpTime;
import com.smartclinicsystem.domain.vo.WeeklySchedule;
import com.smartclinicsystem.domain.vo.WorkingShift;
import jakarta.validation.Valid;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ChangeScheduleCommand {

    @NotNull(message = "Effective date is required")
    @FutureOrPresent(message = "Effective date must be today or in the future")
    private LocalDate validFrom;


    @NotEmpty(message = "Shifts mapping cannot be empty")
    private Map<DayOfWeek, @Valid List<@Valid ShiftDTO>> shifts;

    public static class ShiftDTO {
        @NotNull(message = "Start time is required")
        private LocalTime startTime;

        @NotNull(message = "End time is required")
        private LocalTime endTime;

        public LocalTime getStartTime() { return startTime; }
        public LocalTime getEndTime() { return endTime; }
    }

    public LocalDate getValidFrom() { return validFrom; }
    public Map<DayOfWeek, List<ShiftDTO>> getShifts() { return shifts; }


    public WeeklySchedule toWeeklySchedule() {
        Map<DayOfWeek, List<WorkingShift>> domainShifts = new EnumMap<>(DayOfWeek.class);

        for (Map.Entry<DayOfWeek, List<ShiftDTO>> entry : this.shifts.entrySet()) {
            DayOfWeek day = entry.getKey();
            List<ShiftDTO> dailyShiftDtos = entry.getValue();


            List<WorkingShift> dayShifts = dailyShiftDtos.stream()
                    .map(dto -> new WorkingShift(
                            new SharpTime(dto.getStartTime()),
                            new SharpTime(dto.getEndTime())
                    ))
                    .collect(Collectors.toList());

            domainShifts.put(day, dayShifts);
        }

        return new WeeklySchedule(domainShifts);
    }
}