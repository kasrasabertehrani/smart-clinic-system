package com.smartclinicsystem.domain.vo;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record TimeSlot(LocalDate date, SharpTime start, Duration duration) {

    public TimeSlot {
        if (date == null || start == null || duration == null) {
            throw new IllegalArgumentException("Date, start time, and duration are required.");
        }
    }
    public SharpTime getEnd() {
        return start.plus(duration);
    }

    public LocalDateTime getStartDateTime() {
        return LocalDateTime.of(date, start.time());
    }

    public boolean overlapsWith(TimeSlot other) {
        if (!this.date.equals(other.date())) {
            return false;
        }
        return this.start.isBefore(other.getEnd()) &&
                other.start().isBefore(this.getEnd());
    }
}