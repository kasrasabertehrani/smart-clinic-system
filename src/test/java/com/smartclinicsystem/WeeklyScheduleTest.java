package com.smartclinicsystem;

import com.smartclinicsystem.domain.vo.SharpTime;
import com.smartclinicsystem.domain.vo.TimePeriod;
import com.smartclinicsystem.domain.vo.WeeklySchedule;
import com.smartclinicsystem.domain.vo.WorkingShift;

import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Collections;
import static org.junit.jupiter.api.Assertions.*;

public class WeeklyScheduleTest {

    public static WeeklySchedule createSampleSchedule() {
        Map<DayOfWeek, List<WorkingShift>> weeklyMap = new EnumMap<>(DayOfWeek.class);

        // Monday & Tuesday: 9 AM to 5 PM
        WorkingShift standardShift = new WorkingShift(SharpTime.of(9), SharpTime.of(17));
        weeklyMap.put(DayOfWeek.MONDAY, List.of(standardShift));
        weeklyMap.put(DayOfWeek.TUESDAY, List.of(standardShift));

        // Wednesday: 8 AM to 12 PM
        weeklyMap.put(DayOfWeek.WEDNESDAY, List.of(

                new WorkingShift(SharpTime.of(8), SharpTime.of(12))
        ));

        // Thursday: Split shift (9 AM - 12 PM, 1 PM - 6 PM)
        weeklyMap.put(DayOfWeek.THURSDAY, List.of(
                new WorkingShift(SharpTime.of(9), SharpTime.of(12)),
                new WorkingShift(SharpTime.of(13), SharpTime.of(18))
        ));

        // Friday: 10 AM to 4 PM
        weeklyMap.put(DayOfWeek.FRIDAY, List.of(
                new WorkingShift(SharpTime.of(10), SharpTime.of(16))
        ));

        // Saturday & Sunday: Empty lists (or simply omitted depending on your constructor logic)
        weeklyMap.put(DayOfWeek.SATURDAY, Collections.emptyList());
        weeklyMap.put(DayOfWeek.SUNDAY, Collections.emptyList());

        return new WeeklySchedule(weeklyMap);
    }



    @Test
    void testWeeklyScheduleCreation() {
        // 1. Create the map
        Map<DayOfWeek, List<WorkingShift>> scheduleMap = new EnumMap<>(DayOfWeek.class);

        // 2. Add some dummy data to satisfy the map
        SharpTime start = SharpTime.of(9);
        SharpTime end =   SharpTime.of(17);
        scheduleMap.put(DayOfWeek.MONDAY, List.of(new WorkingShift(start, end)));

        // 3. THIS is the line that gives you code coverage!
        WeeklySchedule schedule = new WeeklySchedule(scheduleMap);

        // 4. Assert it was created successfully
        assertNotNull(schedule);
        assertNotNull(schedule.schedule()); // This tests the auto-generated getter!
    }
    @Test
    void testIsWorkingDuring(){
        WeeklySchedule schedule = createSampleSchedule();
        LocalDateTime startTime = LocalDateTime.of(2026, 5, 19, 10, 0);
        LocalDateTime endTime = LocalDateTime.of(2026, 5, 19, 11, 0);

        TimePeriod timePeriod = new TimePeriod(startTime, endTime);

        assertTrue(schedule.isWorkingDuring(timePeriod));
    }
    @Test
    void testIsWorkingDuringOnNonWorkingHours(){
        WeeklySchedule schedule = createSampleSchedule();
        LocalDateTime startTime = LocalDateTime.of(2026, 5, 20, 12, 0);
        LocalDateTime endTime = LocalDateTime.of(2026, 5, 20, 14, 0);

        TimePeriod timePeriod = new TimePeriod(startTime, endTime);

        assertFalse(schedule.isWorkingDuring(timePeriod));
    }
    @Test
    void testIsWorkingOnSplitShift(){
        WeeklySchedule schedule = createSampleSchedule();
        LocalDateTime startTime = LocalDateTime.of(2026, 5, 21, 10, 0);
        LocalDateTime endTime = LocalDateTime.of(2026, 5, 21, 14, 0);

        TimePeriod timePeriod = new TimePeriod(startTime, endTime);

        assertFalse(schedule.isWorkingDuring(timePeriod));
    }
    @Test
    void testIsWorkingOnDuringBreakTime(){
        WeeklySchedule schedule = createSampleSchedule();
        LocalDateTime startTime = LocalDateTime.of(2026, 5, 21, 12, 0);
        LocalDateTime endTime = LocalDateTime.of(2026, 5, 21, 13, 0);

        TimePeriod timePeriod = new TimePeriod(startTime, endTime);

        assertFalse(schedule.isWorkingDuring(timePeriod));
    }
    @Test
    void testIsWorkingOnDuringNonWorkingDays(){
        WeeklySchedule schedule = createSampleSchedule();
        LocalDateTime startTime = LocalDateTime.of(2026, 5, 23, 10, 0);
        LocalDateTime endTime = LocalDateTime.of(2026, 5, 23, 11, 0);

        TimePeriod timePeriod = new TimePeriod(startTime, endTime);

        assertFalse(schedule.isWorkingDuring(timePeriod));
    }
    @Test
    void testIsWorkingDuringOnWorkingAndNonWorkingHours(){
        WeeklySchedule schedule = createSampleSchedule();
        LocalDateTime startTime = LocalDateTime.of(2026, 5, 20, 10, 0);
        LocalDateTime endTime = LocalDateTime.of(2026, 5, 20, 14, 0);

        TimePeriod timePeriod = new TimePeriod(startTime, endTime);

        assertFalse(schedule.isWorkingDuring(timePeriod));
    }
    @Test
    void testIsWorkingDuringOnTwoConsecutiveWorkingDays(){
        WeeklySchedule schedule = createSampleSchedule();
        LocalDateTime startTime = LocalDateTime.of(2026, 5, 21, 10, 0);
        LocalDateTime endTime = LocalDateTime.of(2026, 5, 22, 11, 0);

        TimePeriod timePeriod = new TimePeriod(startTime, endTime);

        assertFalse(schedule.isWorkingDuring(timePeriod));
    }


}