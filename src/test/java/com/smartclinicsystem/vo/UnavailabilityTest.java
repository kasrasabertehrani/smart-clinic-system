package com.smartclinicsystem.vo;

import com.smartclinicsystem.domain.exception.InvalidTimePeriodException;
import com.smartclinicsystem.domain.vo.TimePeriod;
import com.smartclinicsystem.domain.vo.Unavailability;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static com.smartclinicsystem.TestFixtures.timePeriod;
import static org.junit.jupiter.api.Assertions.*;

public class UnavailabilityTest {
    @Test
    void testUnavailabilityCreation() {
        LocalDateTime start = LocalDateTime.of(2026, 5, 20, 10, 0);
        LocalDateTime end = LocalDateTime.of(2026, 5, 20, 11, 0);
        TimePeriod period = new TimePeriod(start, end);

        Unavailability unavailability = new Unavailability(period);

        assertEquals(period, unavailability.unavailabilityPeriod());
    }
    @Test
    void testUnavailabilityCreationWithNullPeriod() {
        assertThrows(InvalidTimePeriodException.class, () -> new Unavailability(null));
    }
    @Test
    void testIsUnavailableInWithNoOverlap() {
        TimePeriod period1 = timePeriod(2026, 5, 21, 9, 10);
        TimePeriod period2 = timePeriod(2026, 5, 21, 11, 12);
        Unavailability unavailability = new Unavailability(period1);

        assertFalse(unavailability.isUnavailableIn(period2));
    }

    @Test
    void testIsUnavailableInWithPartialOverlap() {
        TimePeriod period1 = timePeriod(2026, 5, 21, 8, 10);
        TimePeriod period2 = timePeriod(2026, 5, 21, 9, 11);
        Unavailability unavailability = new Unavailability(period1);

        assertTrue(unavailability.isUnavailableIn(period2));
    }

    @Test
    void testIsUnavailableInWithCompleteContainment() {
        TimePeriod period1 = timePeriod(2026, 5, 21, 9, 12);
        TimePeriod period2 = timePeriod(2026, 5, 21, 10, 11);

        Unavailability unavailability = new Unavailability(period1);

        assertTrue(unavailability.isUnavailableIn(period2));
    }

    @Test
    void testIsUnavailableInWithIdenticalPeriods() {
        TimePeriod period1 = timePeriod(2026, 5, 21, 9, 10);
        TimePeriod period2 = timePeriod(2026, 5, 21, 9, 10);

        Unavailability unavailability = new Unavailability(period1);

        assertTrue(unavailability.isUnavailableIn(period2));
    }

    @Test
    void testIsUnavailableInWithReversedComparison() {
        TimePeriod period1 = timePeriod(2026, 5, 21, 11, 13);
        TimePeriod period2 = timePeriod(2026, 5, 21, 10, 12);
        Unavailability unavailability1 = new Unavailability(period1);
        Unavailability unavailability2 = new Unavailability(period2);

        assertTrue(unavailability1.isUnavailableIn(period2));
        assertTrue(unavailability2.isUnavailableIn(period2));
    }

    @Test
    void testIsUnavailableInWithDifferentDays() {
        TimePeriod period1 = timePeriod(2026, 5, 21, 9, 10);
        TimePeriod period2 = timePeriod(2026, 5, 22, 9, 10);

        Unavailability unavailability = new Unavailability(period1);

        assertFalse(unavailability.isUnavailableIn(period2));
    }

    @Test
    void testIsUnavailableInWithConnectedPeriods() {
        TimePeriod period1 = timePeriod(2026, 5, 21, 9, 10);
        TimePeriod period2 = timePeriod(2026, 5, 21, 10, 11);

        Unavailability unavailability = new Unavailability(period1);

        assertFalse(unavailability.isUnavailableIn(period2));
    }

    @Test
    void testIsUnavailableInWithConnectedPeriodsReversed() {
        TimePeriod period1 = timePeriod(2026, 5, 21, 11, 12);
        TimePeriod period2 = timePeriod(2026, 5, 21, 10, 11);

        Unavailability unavailability = new Unavailability(period1);

        assertFalse(unavailability.isUnavailableIn(period2));
    }

}
