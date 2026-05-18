package com.smartclinicsystem;

import com.smartclinicsystem.domain.vo.TimePeriod;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDateTime;

public class TimePeriodTest {
    @Test
    void testTimePeriod(){
        LocalDateTime startTime = LocalDateTime.of(2026, 5, 20, 10, 0);
        LocalDateTime endTime = LocalDateTime.of(2026, 5, 20, 11, 0);
        TimePeriod timePeriod = new TimePeriod(startTime, endTime);
        assertEquals(timePeriod.startTime(), startTime);
        assertEquals(timePeriod.endTime(), endTime);
    }
    @Test
    void testTimePeriodOnNullTimeSlot(){
        assertThrows(IllegalArgumentException.class, () -> {
            new TimePeriod(null, null);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            new TimePeriod(LocalDateTime.now(), null);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            new TimePeriod(null, LocalDateTime.now());
        });
    }
    @Test
    void testTimePeriodOnPastTimeSlot(){
        LocalDateTime startTime = LocalDateTime.of(2026, 5, 15, 10, 0);
        LocalDateTime endTime = LocalDateTime.of(2026, 5, 14, 11, 0);
        assertThrows(IllegalArgumentException.class, () -> {
            new TimePeriod(startTime, endTime);
        });
    }
    @Test
    void testTimePeriodIsEqualToStartTime(){
        LocalDateTime startTime = LocalDateTime.of(2026, 5, 20, 10, 0);
        assertThrows(IllegalArgumentException.class, () -> {
            new TimePeriod(startTime, startTime);
        });
    }
    @Test
    void testTimeOnNotSharpHoursForStartTime(){
        LocalDateTime startTime = LocalDateTime.of(2026, 5, 20, 10, 5);
        LocalDateTime endTime = LocalDateTime.of(2026, 5, 20, 12, 0);
        assertThrows(IllegalArgumentException.class, () -> {
            new TimePeriod(startTime, endTime);
        });
    }
    @Test
    void testTimePeriodOnWrongSecondsForStartTime(){
        LocalDateTime startTime = LocalDateTime.of(2026, 5, 20, 10, 0, 5);
        LocalDateTime endTime = LocalDateTime.of(2026, 5, 20, 11, 0, 5);
        assertThrows(IllegalArgumentException.class, () -> {
            new TimePeriod(startTime, endTime);
        });
    }
    @Test
    void testTimePeriodOnWrongNanoSecondsForStartTime(){
        LocalDateTime startTime = LocalDateTime.of(2026, 5, 20, 10, 0, 0, 5);
        LocalDateTime endTime = LocalDateTime.of(2026, 5, 20, 11, 0, 0, 5);
        assertThrows(IllegalArgumentException.class, () -> {
            new TimePeriod(startTime, endTime);
        });
    }
    @Test
    void testTimeOnNotSharpHoursForEndTime(){
        LocalDateTime startTime = LocalDateTime.of(2026, 5, 20, 10, 0);
        LocalDateTime endTime = LocalDateTime.of(2026, 5, 20, 12, 5);
        assertThrows(IllegalArgumentException.class, () -> {
            new TimePeriod(startTime, endTime);
        });
    }
    @Test
    void testTimePeriodOnWrongSecondsForEndTime(){
        LocalDateTime startTime = LocalDateTime.of(2026, 5, 20, 10, 0, 0);
        LocalDateTime endTime = LocalDateTime.of(2026, 5, 20, 11, 0, 5);
        assertThrows(IllegalArgumentException.class, () -> {
            new TimePeriod(startTime, endTime);
        });
    }
    @Test
    void testTimePeriodOnWrongNanoSecondsForEndTime(){
        LocalDateTime startTime = LocalDateTime.of(2026, 5, 20, 10, 0, 0, 0);
        LocalDateTime endTime = LocalDateTime.of(2026, 5, 20, 11, 0, 0, 5);
        assertThrows(IllegalArgumentException.class, () -> {
            new TimePeriod(startTime, endTime);
        });
    }
    @Test
    void testOverlapsWithNoOverlap() {
        TimePeriod period1 = new TimePeriod(
                LocalDateTime.of(2026, 5, 18, 9, 0),
                LocalDateTime.of(2026, 5, 18, 10, 0)
        );
        TimePeriod period2 = new TimePeriod(
                LocalDateTime.of(2026, 5, 18, 11, 0),
                LocalDateTime.of(2026, 5, 18, 12, 0)
        );

        assertFalse(period1.overlapsWith(period2));
    }

    @Test
    void testOverlapsWithAdjacentPeriodsNoOverlap() {
        TimePeriod period1 = new TimePeriod(
                LocalDateTime.of(2026, 5, 18, 9, 0),
                LocalDateTime.of(2026, 5, 18, 10, 0)
        );
        TimePeriod period2 = new TimePeriod(
                LocalDateTime.of(2026, 5, 18, 10, 0),
                LocalDateTime.of(2026, 5, 18, 11, 0)
        );

        assertFalse(period1.overlapsWith(period2));
    }

    @Test
    void testOverlapsWithPartialOverlap() {
        TimePeriod period1 = new TimePeriod(
                LocalDateTime.of(2026, 5, 18, 8, 0),
                LocalDateTime.of(2026, 5, 18, 10, 0)
        );
        TimePeriod period2 = new TimePeriod(
                LocalDateTime.of(2026, 5, 18, 9, 0),
                LocalDateTime.of(2026, 5, 18, 11, 0)
        );

        assertTrue(period1.overlapsWith(period2));
    }

    @Test
    void testOverlapsWithCompleteContainment() {
        TimePeriod period1 = new TimePeriod(
                LocalDateTime.of(2026, 5, 18, 9, 0),
                LocalDateTime.of(2026, 5, 18, 12, 0)
        );
        TimePeriod period2 = new TimePeriod(
                LocalDateTime.of(2026, 5, 18, 10, 0),
                LocalDateTime.of(2026, 5, 18, 11, 0)
        );

        assertTrue(period1.overlapsWith(period2));
    }

    @Test
    void testOverlapsWithIdenticalPeriods() {
        LocalDateTime start = LocalDateTime.of(2026, 5, 18, 9, 0);
        LocalDateTime end = LocalDateTime.of(2026, 5, 18, 10, 0);

        TimePeriod period1 = new TimePeriod(start, end);
        TimePeriod period2 = new TimePeriod(start, end);

        assertTrue(period1.overlapsWith(period2));
    }

    @Test
    void testOverlapsWithReversedComparison() {
        TimePeriod period1 = new TimePeriod(
                LocalDateTime.of(2026, 5, 18, 11, 0),
                LocalDateTime.of(2026, 5, 18, 13, 0)
        );
        TimePeriod period2 = new TimePeriod(
                LocalDateTime.of(2026, 5, 18, 10, 0),
                LocalDateTime.of(2026, 5, 18, 12, 0)
        );

        assertTrue(period1.overlapsWith(period2));
        assertTrue(period2.overlapsWith(period1));
    }

    @Test
    void testOverlapsWithDifferentDays() {
        TimePeriod period1 = new TimePeriod(
                LocalDateTime.of(2026, 5, 18, 9, 0),
                LocalDateTime.of(2026, 5, 18, 10, 0)
        );
        TimePeriod period2 = new TimePeriod(
                LocalDateTime.of(2026, 5, 19, 9, 0),
                LocalDateTime.of(2026, 5, 19, 10, 0)
        );

        assertFalse(period1.overlapsWith(period2));
    }
    @Test
    void testOverlapsWithNoOverlapSeparatePeriods() {
        TimePeriod period1 = new TimePeriod(
                LocalDateTime.of(2026, 5, 18, 9, 0),
                LocalDateTime.of(2026, 5, 18, 10, 0)
        );
        TimePeriod period2 = new TimePeriod(
                LocalDateTime.of(2026, 5, 18, 11, 0),
                LocalDateTime.of(2026, 5, 18, 12, 0)
        );

        assertFalse(period1.overlapsWith(period2));
    }

    @Test
    void testOverlapsWithNoOverlapMuchLaterPeriod() {
        TimePeriod period1 = new TimePeriod(
                LocalDateTime.of(2026, 5, 18, 8, 0),
                LocalDateTime.of(2026, 5, 18, 9, 0)
        );
        TimePeriod period2 = new TimePeriod(
                LocalDateTime.of(2026, 5, 18, 14, 0),
                LocalDateTime.of(2026, 5, 18, 15, 0)
        );

        assertFalse(period1.overlapsWith(period2));
    }

    @Test
    void testOverlapsWithAdjacentPeriodsNoOverlap1() {
        TimePeriod period1 = new TimePeriod(
                LocalDateTime.of(2026, 5, 18, 9, 0),
                LocalDateTime.of(2026, 5, 18, 10, 0)
        );
        TimePeriod period2 = new TimePeriod(
                LocalDateTime.of(2026, 5, 18, 10, 0),
                LocalDateTime.of(2026, 5, 18, 11, 0)
        );

        assertFalse(period1.overlapsWith(period2));
    }

    @Test
    void testOverlapsWithAdjacentReverseNoOverlap() {
        TimePeriod period1 = new TimePeriod(
                LocalDateTime.of(2026, 5, 18, 10, 0),
                LocalDateTime.of(2026, 5, 18, 11, 0)
        );
        TimePeriod period2 = new TimePeriod(
                LocalDateTime.of(2026, 5, 18, 9, 0),
                LocalDateTime.of(2026, 5, 18, 10, 0)
        );

        assertFalse(period1.overlapsWith(period2));
    }

    @Test
    void testOverlapsWithMultipleHoursApart() {
        TimePeriod period1 = new TimePeriod(
                LocalDateTime.of(2026, 5, 18, 7, 0),
                LocalDateTime.of(2026, 5, 18, 8, 0)
        );
        TimePeriod period2 = new TimePeriod(
                LocalDateTime.of(2026, 5, 18, 12, 0),
                LocalDateTime.of(2026, 5, 18, 13, 0)
        );

        assertFalse(period1.overlapsWith(period2));
    }

    @Test
    void testOverlapsWithDifferentDaysNoOverlap() {
        TimePeriod period1 = new TimePeriod(
                LocalDateTime.of(2026, 5, 18, 9, 0),
                LocalDateTime.of(2026, 5, 18, 10, 0)
        );
        TimePeriod period2 = new TimePeriod(
                LocalDateTime.of(2026, 5, 19, 9, 0),
                LocalDateTime.of(2026, 5, 19, 10, 0)
        );

        assertFalse(period1.overlapsWith(period2));
    }

    @Test
    void testOverlapsWithDifferentMonthsNoOverlap() {
        TimePeriod period1 = new TimePeriod(
                LocalDateTime.of(2026, 5, 18, 9, 0),
                LocalDateTime.of(2026, 5, 18, 10, 0)
        );
        TimePeriod period2 = new TimePeriod(
                LocalDateTime.of(2026, 6, 18, 9, 0),
                LocalDateTime.of(2026, 6, 18, 10, 0)
        );

        assertFalse(period1.overlapsWith(period2));
    }

}
