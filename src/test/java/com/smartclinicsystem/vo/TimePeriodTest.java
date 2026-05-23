package com.smartclinicsystem.vo;

import com.smartclinicsystem.domain.exception.InvalidTimePeriodException;
import com.smartclinicsystem.domain.exception.NotSharpTimeException;
import com.smartclinicsystem.domain.vo.TimePeriod;
import org.junit.jupiter.api.Test;


import java.time.LocalDateTime;

import static com.smartclinicsystem.TestFixtures.*;
import static org.junit.jupiter.api.Assertions.*;

public class TimePeriodTest {
    @Test
    void testTimePeriodCreation() {
        TimePeriod period = timePeriod(2026, 5, 20, 10, 11);

        assertEquals(LocalDateTime.of(2026, 5, 20, 10, 0), period.startTime());
        assertEquals(LocalDateTime.of(2026, 5, 20, 11, 0), period.endTime());
    }

    @Test
    void testTimePeriodOnNullTimeSlot() {
        assertThrows(InvalidTimePeriodException.class, () -> 
            new TimePeriod(null, null)
        );
        assertThrows(InvalidTimePeriodException.class, () -> 
            new TimePeriod(LocalDateTime.of(2026, 5, 20, 10, 0), null)
        );
        assertThrows(InvalidTimePeriodException.class, () -> 
            new TimePeriod(null, LocalDateTime.of(2026, 5, 20, 10, 0))
        );
    }

    @Test
    void testTimePeriodOnPastTimeSlot() {
        assertThrows(InvalidTimePeriodException.class, () ->
            new TimePeriod(
                LocalDateTime.of(2026, 5, 15, 10, 0),
                LocalDateTime.of(2026, 5, 14, 11, 0)
            )
        );
    }

    @Test
    void testTimePeriodIsEqualToStartTime() {
        LocalDateTime startTime = LocalDateTime.of(2026, 5, 20, 10, 0);

        assertThrows(InvalidTimePeriodException.class, () ->
            new TimePeriod(startTime, startTime)
        );
    }

    @Test
    void testTimeOnNotSharpHoursForStartTime() {
        assertThrows(NotSharpTimeException.class, () ->
            new TimePeriod(
                LocalDateTime.of(2026, 5, 20, 10, 5),
                LocalDateTime.of(2026, 5, 20, 12, 0)
            )
        );
    }

    @Test
    void testTimeOnNotSharpHoursForEndTime() {
        assertThrows(NotSharpTimeException.class, () ->
            new TimePeriod(
                LocalDateTime.of(2026, 5, 20, 10, 0),
                LocalDateTime.of(2026, 5, 20, 12, 5)
            )
        );
    }

    // ==================== Overlap Tests ====================

    @Test
    void testOverlapsWithNoOverlap() {
        TimePeriod period1 = timePeriod(2026, 5, 21, 9, 10);
        TimePeriod period2 = timePeriod(2026, 5, 21, 11, 12);

        assertFalse(period1.overlapsWith(period2));
    }

    @Test
    void testOverlapsWithPartialOverlap() {
        TimePeriod period1 = timePeriod(2026, 5, 21, 8, 10);
        TimePeriod period2 = timePeriod(2026, 5, 21, 9, 11);

        assertTrue(period1.overlapsWith(period2));
    }

    @Test
    void testOverlapsWithCompleteContainment() {
        TimePeriod period1 = timePeriod(2026, 5, 21, 9, 12);
        TimePeriod period2 = timePeriod(2026, 5, 21, 10, 11);

        assertTrue(period1.overlapsWith(period2));
    }

    @Test
    void testOverlapsWithIdenticalPeriods() {
        TimePeriod period1 = timePeriod(2026, 5, 21, 9, 10);
        TimePeriod period2 = timePeriod(2026, 5, 21, 9, 10);

        assertTrue(period1.overlapsWith(period2));
    }

    @Test
    void testOverlapsWithReversedComparison() {
        TimePeriod period1 = timePeriod(2026, 5, 21, 11, 13);
        TimePeriod period2 = timePeriod(2026, 5, 21, 10, 12);

        assertTrue(period1.overlapsWith(period2));
        assertTrue(period2.overlapsWith(period1));
    }

    @Test
    void testOverlapsWithDifferentDays() {
        TimePeriod period1 = timePeriod(2026, 5, 21, 9, 10);
        TimePeriod period2 = timePeriod(2026, 5, 22, 9, 10);

        assertFalse(period1.overlapsWith(period2));
    }

    @Test
    void testOverlapsWithConnectedPeriods() {
        TimePeriod period1 = timePeriod(2026, 5, 21, 9, 10);
        TimePeriod period2 = timePeriod(2026, 5, 21, 10, 11);

        assertFalse(period1.overlapsWith(period2));
    }

    @Test
    void testOverlapsWithConnectedPeriodsReversed() {
        TimePeriod period1 = timePeriod(2026, 5, 21, 11, 12);
        TimePeriod period2 = timePeriod(2026, 5, 21, 10, 11);

        assertFalse(period1.overlapsWith(period2));
    }



}
