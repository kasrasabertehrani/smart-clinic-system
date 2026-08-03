package com.smartclinicsystem.domain.vo;

import com.smartclinicsystem.domain.exception.InvalidTimePeriodException;


public record Unavailability(TimePeriod unavailabilityPeriod) {

public Unavailability {
    if (unavailabilityPeriod == null) {
        throw new InvalidTimePeriodException("Unavailability period cannot be null.");
    }
}

    public boolean isUnavailableIn(TimeSlot requestedTimeSlot) {
        TimePeriod requestedPeriod = new TimePeriod(requestedTimeSlot.getStartDateTime(),
                requestedTimeSlot.getEndDateTime());
        return this.unavailabilityPeriod.overlapsWith(requestedPeriod);
    }

}
