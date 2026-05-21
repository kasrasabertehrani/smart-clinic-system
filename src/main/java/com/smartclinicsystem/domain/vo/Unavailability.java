package com.smartclinicsystem.domain.vo;

import com.smartclinicsystem.domain.exception.InvalidTimePeriodException;


public record Unavailability(TimePeriod unavailabilityPeriod) {

public Unavailability {
    if (unavailabilityPeriod == null) {
        throw new InvalidTimePeriodException("Unavailability period cannot be null.");
    }
}

    public boolean isUnavailableIn(TimePeriod other) {
        return this.unavailabilityPeriod.overlapsWith(other);
    }

}
