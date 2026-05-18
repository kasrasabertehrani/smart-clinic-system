package com.smartclinicsystem.domain;

import com.smartclinicsystem.domain.vo.TimePeriod;
import lombok.Getter;
import java.time.LocalDateTime;



@Getter
public class Unavailability {
    private final TimePeriod unavailabilityPeriod;
    private final String reason;
    private final LocalDateTime createdAt;

    public Unavailability(TimePeriod unavailabilityPeriod, String reason) {
        this.unavailabilityPeriod = unavailabilityPeriod;
        this.reason = reason;
        this.createdAt = LocalDateTime.now();
    }

    public boolean isUnavailableIn(TimePeriod other) {
        return this.unavailabilityPeriod.overlapsWith(other);
    }

}
