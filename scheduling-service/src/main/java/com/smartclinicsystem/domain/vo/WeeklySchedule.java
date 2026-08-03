package com.smartclinicsystem.domain.vo;

import com.smartclinicsystem.domain.exception.InvalidWeeklyScheduleException;

import java.time.DayOfWeek;
import java.util.*;

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

            List<WorkingShift> shiftsForDay = schedule.get(day);
            if (shiftsForDay == null) {
                throw new InvalidWeeklyScheduleException(
                        "The shift list for " + day + " cannot be null. Use an empty list instead."
                );
            }
            if (shiftsForDay.size() > 1) {
                List<WorkingShift> sortedShifts = new ArrayList<>(shiftsForDay);
                sortedShifts.sort(Comparator.comparing(shift -> shift.startTime().time()));

                for (int i = 0; i < sortedShifts.size() - 1; i++) {
                    WorkingShift currentShift = sortedShifts.get(i);
                    WorkingShift nextShift = sortedShifts.get(i + 1);

                    if (currentShift.overlapsWith(nextShift)) {
                        throw new InvalidWeeklyScheduleException(
                                "Overlapping shifts detected on " + day +
                                        ": " + currentShift + " overlaps with " + nextShift
                        );
                    }
                }
            }
        }

        schedule = Collections.unmodifiableMap(schedule);
    }

    public boolean isWorkingDuring(TimeSlot requestedPeriod) {

        DayOfWeek requestedDay = requestedPeriod.date().getDayOfWeek();
        List<WorkingShift> shiftsForDay = schedule.getOrDefault(requestedDay, List.of());
        return shiftsForDay.stream()
                .anyMatch(shift -> shift.covers(requestedPeriod.start().time(),
                        requestedPeriod.getEnd().time()));
    }
}