package com.smartclinicsystem.domain.vo;

import com.smartclinicsystem.domain.exception.NotSharpTimeException;

import java.time.Duration;
import java.time.LocalTime;

public record SharpTime(LocalTime time) {

    private static final int GRANULARITY_MINUTES = 15;

    public SharpTime {
        validateIsSharp(time);
    }

    public static void validateIsSharp(LocalTime time) {
        if (time.getSecond() != 0 || time.getNano() != 0) {
            throw new NotSharpTimeException("Time cannot have seconds or nanoseconds.");
        }
        if (time.getMinute() % GRANULARITY_MINUTES != 0) {
            throw new NotSharpTimeException("Time must be in 15-minute increments.");
        }

    }
    public static SharpTime of(int hour, int minute) {
        if (hour < 0 || hour > 23) {
            throw new IllegalArgumentException("Hour must be between 0 and 23.");
        }
        if (minute < 0 || minute > 59) {
            throw new IllegalArgumentException("Minute must be between 0 and 59.");
        }
        return new SharpTime(LocalTime.of(hour, minute));
    }
    public boolean isBefore(SharpTime other) {
        return this.time.isBefore(other.time());
    }

    public boolean isAfter(SharpTime other) {
        return this.time.isAfter(other.time());
    }

    public SharpTime plus(Duration duration) {
        return new SharpTime(this.time.plus(duration));
    }
}