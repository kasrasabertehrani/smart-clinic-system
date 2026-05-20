package com.smartclinicsystem.domain.vo;

import com.smartclinicsystem.domain.exception.NotSharpTimeException;

import java.time.LocalTime;

public record SharpTime(LocalTime time) {

    public SharpTime {
        validateIsSharp(time);
    }

    public static void validateIsSharp(LocalTime time) {
        if (time.getMinute() != 0 || time.getSecond() != 0 || time.getNano() != 0) {
            throw new NotSharpTimeException();
        }

    }
    public static SharpTime of(int hour) {
        if (hour < 0 || hour > 23) {
            throw new IllegalArgumentException("Hour must be between 0 and 23.");
        }
        return new SharpTime(LocalTime.of(hour, 0));
    }
}