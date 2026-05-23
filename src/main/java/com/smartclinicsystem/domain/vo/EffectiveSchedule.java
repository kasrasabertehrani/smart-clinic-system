package com.smartclinicsystem.domain.vo;

import com.smartclinicsystem.domain.exception.InvalidEffectiveScheduleException;

import java.time.LocalDate;

public record EffectiveSchedule(LocalDate validFrom, WeeklySchedule schedule) {

    public EffectiveSchedule {
        if (validFrom == null) {
            throw new InvalidEffectiveScheduleException("An effective schedule must have a valid starting date.");
        }
        if (schedule == null) {
            throw new InvalidEffectiveScheduleException("An effective schedule must contain a valid WeeklySchedule.");
        }
    }

    public boolean appliesTo(LocalDate date) {
        return !date.isBefore(validFrom);
    }
}