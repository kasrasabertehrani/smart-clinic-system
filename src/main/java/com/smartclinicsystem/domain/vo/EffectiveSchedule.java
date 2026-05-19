package com.smartclinicsystem.domain.vo;

import java.time.LocalDate;

public record EffectiveSchedule(LocalDate validFrom, WeeklySchedule schedule) {

    public boolean appliesTo(LocalDate date) {
        return date.isAfter(validFrom);
    }
}