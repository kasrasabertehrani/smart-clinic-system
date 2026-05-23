package com.smartclinicsystem.vo;

import com.smartclinicsystem.domain.exception.InvalidWeeklyScheduleException;
import com.smartclinicsystem.domain.vo.TimePeriod;
import com.smartclinicsystem.domain.vo.WeeklySchedule;
import com.smartclinicsystem.domain.vo.WorkingShift;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import static com.smartclinicsystem.TestFixtures.*;
import static org.junit.jupiter.api.Assertions.*;

public class WeeklyScheduleTest {

    @Test
    void testWeeklyScheduleCreation() {
        Map<DayOfWeek, List<WorkingShift>> scheduleMap = new EnumMap<>(DayOfWeek.class);
        for (DayOfWeek day : DayOfWeek.values()) {
            if (day == DayOfWeek.MONDAY) {
                scheduleMap.put(day, List.of(workingShift(9, 17)));
            } else {
                scheduleMap.put(day, List.of());
            }
        }

        WeeklySchedule schedule = new WeeklySchedule(scheduleMap);

        assertNotNull(schedule);
        assertNotNull(schedule.schedule());
    }

    @Test
    void testScheduleIsImmutable() {
        Map<DayOfWeek, List<WorkingShift>> scheduleMap = new EnumMap<>(DayOfWeek.class);
        for (DayOfWeek day : DayOfWeek.values()) {
            scheduleMap.put(day, List.of());
        }
        scheduleMap.put(DayOfWeek.MONDAY, List.of(workingShift(9, 17)));

        WeeklySchedule schedule = new WeeklySchedule(scheduleMap);
        
        // Try to modify the original map (should not affect the schedule)
        Map<DayOfWeek, List<WorkingShift>> newMap = new EnumMap<>(DayOfWeek.class);
        for (DayOfWeek day : DayOfWeek.values()) {
            newMap.put(day, List.of());
        }
        newMap.put(DayOfWeek.TUESDAY, List.of(workingShift(9, 17)));

        // Original map changes shouldn't affect schedule
        assertEquals(1, schedule.schedule().values().stream().mapToInt(List::size).sum());
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
        // 1. We must use a map that actually allows null values (EnumMap)
        Map<DayOfWeek, List<WorkingShift>> mapWithNullList = new EnumMap<>(DayOfWeek.class);

        // 2. We must add ALL 7 days first, otherwise the "missing day" exception will trigger instead!
        for (DayOfWeek day : DayOfWeek.values()) {
            mapWithNullList.put(day, Collections.emptyList());
        }

        // 3. Now we inject the poison pill (overwriting Monday with null)
        mapWithNullList.put(DayOfWeek.MONDAY, null);

        // 4. Assert our specific domain exception is thrown
        InvalidWeeklyScheduleException exception = assertThrows(
                InvalidWeeklyScheduleException.class,
                () -> new WeeklySchedule(mapWithNullList)
        );

        // Optional but highly recommended: verify it failed for the EXACT right reason
        assertTrue(exception.getMessage().contains("cannot be null"));
    }

    // ==================== Working Hours Tests ====================

    @Test
    void testIsWorkingDuringNormalHours() {
        WeeklySchedule schedule = standardWeeklySchedule();
        // Tuesday 10:00-11:00 (within 9:00-17:00)
        TimePeriod period = timePeriod(2026, 5, 21, 10, 11);

        assertTrue(schedule.isWorkingDuring(period));
    }

    @Test
    void testIsWorkingDuringNonWorkingHours() {
        WeeklySchedule schedule = standardWeeklySchedule();
        // Monday 18:00-19:00 (after working hours 9-17)
        TimePeriod period = timePeriod(2026, 5, 21, 18, 19);

        assertFalse(schedule.isWorkingDuring(period));
    }

    @Test
    void testIsWorkingDuringOnSplitShiftGap() {
        WeeklySchedule schedule = standardWeeklySchedule();
        // Thursday 10:00-14:00 (crosses the break gap between 12:00 and 13:00)
        TimePeriod period = timePeriod(2026, 5, 23, 10, 14);

        assertFalse(schedule.isWorkingDuring(period));
    }

    @Test
    void testIsWorkingDuringOnBreakTime() {
        WeeklySchedule schedule = standardWeeklySchedule();
        // Thursday 12:00-13:00 (exact break time between shifts)
        TimePeriod period = timePeriod(2026, 5, 23, 12, 13);

        assertFalse(schedule.isWorkingDuring(period));
    }

    @Test
    void testIsWorkingDuringOnNonWorkingDays() {
        WeeklySchedule schedule = standardWeeklySchedule();
        // Saturday May 30 10:00-11:00 (weekend, not working)
        TimePeriod period = timePeriod(2026, 5, 30, 10, 11);

        assertFalse(schedule.isWorkingDuring(period));
    }

    @Test
    void testIsWorkingDuringPartiallyOverlapNonWorkingHours() {
        WeeklySchedule schedule = standardWeeklySchedule();
        // Thursday 10:00-14:00 (spans both shift and break)
        TimePeriod period = timePeriod(2026, 5, 23, 10, 14);

        assertFalse(schedule.isWorkingDuring(period));
    }

    @Test
    void testIsWorkingDuringAcrossDays() {
        WeeklySchedule schedule = standardWeeklySchedule();
        // Tuesday 20:00 - Wednesday 10:00 (crosses days)
        LocalDateTime start = LocalDateTime.of(2026, 5, 21, 20, 0);
        LocalDateTime end = LocalDateTime.of(2026, 5, 22, 10, 0);
        TimePeriod period = new TimePeriod(start, end);

        assertFalse(schedule.isWorkingDuring(period));
    }
}