package com.smartclinicsystem.domain.vo;


import com.smartclinicsystem.domain.exception.InvalidTimePeriodException;

public record WorkingShift(SharpTime startTime, SharpTime endTime) {

    public WorkingShift {
        if (startTime == null || endTime == null) {
            throw new InvalidTimePeriodException("Start and end times are required.");
        }
        if (!endTime.time().isAfter(startTime.time())) {
            throw new InvalidTimePeriodException("End time must be after start time.");
        }
    }

    public boolean covers(TimePeriod period) {

        return !period.startTime().toLocalTime().isBefore(this.startTime.time()) &&
                !period.endTime().toLocalTime().isAfter(this.endTime.time());
    }
}