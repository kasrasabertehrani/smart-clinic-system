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
}