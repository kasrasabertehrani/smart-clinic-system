package com.smartclinicsystem.domain;

import com.smartclinicsystem.domain.vo.TimePeriod;
import lombok.Getter;
import java.time.LocalDateTime;



@Getter
public class Unavailability {
    private final TimePeriod unavailabilityPeriod;;
    private final LocalDateTime createdAt;

    public Unavailability(TimePeriod unavailabilityPeriod) {
        this.unavailabilityPeriod = unavailabilityPeriod;
        this.createdAt = LocalDateTime.now();
    }

    public boolean isUnavailableIn(TimePeriod other) {
        return this.unavailabilityPeriod.overlapsWith(other);
    }

}
