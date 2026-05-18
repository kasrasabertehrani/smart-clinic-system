package com.smartclinicsystem.domain.vo;

import java.time.LocalDate;

// This maps a schedule to a specific starting date
public record EffectiveSchedule(LocalDate validFrom, WeeklySchedule schedule) {

    // Helper method to check if this rule applies to a specific date
    public boolean appliesTo(LocalDate date) {
        return date.isAfter(validFrom);
    }
}