package com.smartclinicsystem.domain.vo;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Map;
import java.util.Collections;

public record WeeklySchedule(Map<DayOfWeek, List<WorkingShift>> schedule) {

    public WeeklySchedule {
        schedule = Collections.unmodifiableMap(schedule);
    }

    public boolean isWorkingDuring(TimePeriod requestedPeriod) {

        if (!requestedPeriod.startTime().toLocalDate().equals(requestedPeriod.endTime().toLocalDate())) {
            return false;
        }
        DayOfWeek requestedDay = requestedPeriod.startTime().getDayOfWeek();
        List<WorkingShift> shiftsForDay = schedule.getOrDefault(requestedDay, List.of());
        return shiftsForDay.stream()
                .anyMatch(shift -> shift.covers(requestedPeriod.startTime().toLocalTime(),
                        requestedPeriod.endTime().toLocalTime()));
    }
}