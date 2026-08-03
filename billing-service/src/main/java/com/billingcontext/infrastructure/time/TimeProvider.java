package com.billingcontext.infrastructure.time;

import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Component
public class TimeProvider {

    private final Clock clock;

    public TimeProvider(Clock clock) {
        this.clock = clock;
    }

    public LocalDateTime getStartOfToday() {
        return LocalDate.now(clock).atStartOfDay();
    }

    public LocalDateTime getEndOfToday() {
        return LocalDate.now(clock).atTime(LocalTime.MAX);
    }

}
