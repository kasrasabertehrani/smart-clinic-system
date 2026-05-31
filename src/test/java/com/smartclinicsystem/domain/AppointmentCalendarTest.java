package com.smartclinicsystem.domain;

import com.smartclinicsystem.domain.exception.BookingException;
import com.smartclinicsystem.domain.exception.InvalidEffectiveScheduleException;
import com.smartclinicsystem.domain.exception.AppointmentNotFoundException;
import com.smartclinicsystem.domain.vo.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static com.smartclinicsystem.domain.TestFixtures.*;

public class AppointmentCalendarTest {

    private AppointmentCalendar calendar;
    @BeforeEach
    void setUp() {
        this.calendar = CalendarFixtures.customCalendar(TestFixtures.doctorId("doc-1"));
    }


}
