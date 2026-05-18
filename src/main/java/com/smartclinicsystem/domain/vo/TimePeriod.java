package com.smartclinicsystem.domain.vo;

import java.time.LocalDateTime;

public record TimePeriod(LocalDateTime startTime, LocalDateTime endTime) {

    public TimePeriod {
        if (startTime == null || endTime == null) {
            throw new IllegalArgumentException("Start and end times are required.");
        }
        if (endTime.isBefore(startTime) || endTime.isEqual(startTime)) {
            throw new IllegalArgumentException("End time must be strictly after start time.");
        }
        SharpTime.validateIsSharp(startTime.toLocalTime());
        SharpTime.validateIsSharp(endTime.toLocalTime());

    }
    public boolean overlapsWith(TimePeriod other) {
        return this.startTime.isBefore(other.endTime) && other.startTime.isBefore(this.endTime);
    }
}