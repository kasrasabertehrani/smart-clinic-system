package com.smartclinicsystem.domain.vo;

import com.smartclinicsystem.domain.exception.InvalidWeeklyScheduleException;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Map;
import java.util.Collections;

public record WeeklySchedule(Map<DayOfWeek, List<WorkingShift>> schedule) {

    public WeeklySchedule {
        if (schedule == null) {
            throw new InvalidWeeklyScheduleException("Weekly schedule map cannot be null.");
        }
        for (DayOfWeek day : DayOfWeek.values()) {
            if (!schedule.containsKey(day)) {
                throw new InvalidWeeklyScheduleException(
                        "Schedule must explicitly contain an entry for " + day + ". Use an empty list for days off."
                );
            }

            if (schedule.get(day) == null) {
                throw new InvalidWeeklyScheduleException(
                        "The shift list for " + day + " cannot be null. Use an empty list instead."
                );
            }
        }
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