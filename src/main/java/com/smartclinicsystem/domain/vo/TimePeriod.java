package com.smartclinicsystem.domain.vo;

import com.smartclinicsystem.domain.exception.InvalidTimePeriodException;

import java.time.LocalDateTime;

public record TimePeriod(LocalDateTime startTime, LocalDateTime endTime) {

    public TimePeriod {
        if (startTime == null || endTime == null) {
            throw new InvalidTimePeriodException("Start and end times are required.");
        }
        if (endTime.isBefore(startTime) || endTime.isEqual(startTime)) {
            throw new InvalidTimePeriodException("End time must be strictly after start time.");
        }
        SharpTime.validateIsSharp(startTime.toLocalTime());
        SharpTime.validateIsSharp(endTime.toLocalTime());

    }
    public boolean overlapsWith(TimePeriod other) {
        return this.startTime.isBefore(other.endTime) && other.startTime.isBefore(this.endTime);
    }
}