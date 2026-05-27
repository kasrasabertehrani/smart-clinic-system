package com.smartclinicsystem.domain.vo;

import com.smartclinicsystem.domain.TestFixtures;
import com.smartclinicsystem.domain.exception.InvalidWeeklyScheduleException;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.util.EnumMap;
import java.util.List;


import static com.smartclinicsystem.domain.TestFixtures.*;
import static org.junit.jupiter.api.Assertions.*;

public class WeeklyScheduleTest {

    @Test
    void testWeeklyScheduleCreation() {
        WeeklySchedule schedule = TestFixtures.scheduleBuilder()
                .withStandardWeekdays(9, 15, 17, 30)
                .withShift(DayOfWeek.SATURDAY, 10, 0, 14, 0)
                .build();

        assertNotNull(schedule);
        assertNotNull(schedule.schedule());
    }


    @Test
    void testScheduleCannotBeModifiedDirectly() {
        WeeklySchedule schedule = standardWeeklySchedule();

        assertThrows(UnsupportedOperationException.class, () ->
            schedule.schedule().put(DayOfWeek.MONDAY, List.of(workingShift(9, 17)))
        );
    }
    @Test
    void testScheduleCannotHaveNullMap() {
        assertThrows(InvalidWeeklyScheduleException.class, () -> new WeeklySchedule(null));
    }
    @Test
    void testScheduleCannotHaveEmptyMap() {
        assertThrows(InvalidWeeklyScheduleException.class, () -> new WeeklySchedule(new EnumMap<>(DayOfWeek.class)));
    }
    @Test
    void testScheduleCannotHaveNullWorkingShiftList() {
        InvalidWeeklyScheduleException exception = assertThrows(
                InvalidWeeklyScheduleException.class,
                () -> TestFixtures.scheduleBuilder()
                        .withNullShiftList(DayOfWeek.MONDAY)
                        .build()
        );

        assertTrue(exception.getMessage().contains("cannot be null"));
    }
    @Test
    void testWorkingShiftsOverlap(){
       assertThrows(InvalidWeeklyScheduleException.class, () -> TestFixtures.scheduleBuilder()
                .withShift(DayOfWeek.MONDAY, 9, 0, 12, 0)
                .withShift(DayOfWeek.MONDAY, 11, 0, 14, 0)
                .build()
       );
    }
    @Test
    void testIsWorkingDuringNormalHours() {
        WeeklySchedule schedule = standardWeeklySchedule();
        TimeSlot slot = timeSlot(6, 10, 9, 15);

        assertTrue(schedule.isWorkingDuring(slot));
    }

    @Test
    void testIsWorkingDuringNonWorkingHours() {
        WeeklySchedule schedule = standardWeeklySchedule();
        TimeSlot slot = timeSlot(6, 10, 8, 0);

        assertFalse(schedule.isWorkingDuring(slot));
    }

    @Test
    void testIsWorkingDuringOnSplitShiftGap() {
        WeeklySchedule schedule = standardWeeklySchedule();
        TimeSlot slot = timeSlot(6, 10, 11, 0, 120);

        assertFalse(schedule.isWorkingDuring(slot));
    }

    @Test
    void testIsWorkingDuringOnBreakTime() {
        WeeklySchedule schedule = standardWeeklySchedule();
        TimeSlot slot = timeSlot(6, 10, 12, 0);

        assertFalse(schedule.isWorkingDuring(slot));
    }

    @Test
    void testIsWorkingDuringOnNonWorkingDays() {
        WeeklySchedule schedule = standardWeeklySchedule();
        TimeSlot slot = timeSlot(6, 13, 10, 0);

        assertFalse(schedule.isWorkingDuring(slot));
    }

}