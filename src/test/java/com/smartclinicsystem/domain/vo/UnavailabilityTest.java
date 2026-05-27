package com.smartclinicsystem.domain.vo;

import com.smartclinicsystem.domain.exception.InvalidTimePeriodException;
import org.junit.jupiter.api.Test;



import static com.smartclinicsystem.domain.TestFixtures.timePeriod;
import static com.smartclinicsystem.domain.TestFixtures.timeSlot;
import static org.junit.jupiter.api.Assertions.*;

public class UnavailabilityTest {
    @Test
    void testUnavailabilityCreation() {
        TimePeriod period = timePeriod(6, 5, 20,6, 25, 13);

        Unavailability unavailability = new Unavailability(period);

        assertEquals(period, unavailability.unavailabilityPeriod());
    }
    @Test
    void testUnavailabilityCreationWithNullPeriod() {
        assertThrows(InvalidTimePeriodException.class, () -> new Unavailability(null));
    }
    @Test
    void testIsUnavailableInWithNoOverlap() {
        TimePeriod period = timePeriod(6, 15, 20,6, 25, 13);
        TimeSlot slot = timeSlot(6, 5, 21, 0);
        Unavailability unavailability = new Unavailability(period);

        assertFalse(unavailability.isUnavailableIn(slot));
    }

    @Test
    void testIsUnavailableInWithPartialOverlap() {
        TimePeriod period = timePeriod(6, 5, 20,6, 25, 13);
        TimeSlot slot = timeSlot(6, 5, 19, 15, 90);
        Unavailability unavailability = new Unavailability(period);

        assertTrue(unavailability.isUnavailableIn(slot));
    }

    @Test
    void testIsUnavailableInWithCompleteContainment() {
        TimePeriod period = timePeriod(6, 5, 20,6, 25, 13);
        TimeSlot slot = timeSlot(6, 5, 21, 0);

        Unavailability unavailability = new Unavailability(period);

        assertTrue(unavailability.isUnavailableIn(slot));
    }

    @Test
    void testIsUnavailableInWithIdenticalPeriods() {
        TimePeriod period = timePeriod(2026, 5, 5, 9, 10);
        TimeSlot slot = timeSlot(5, 5, 9, 0);

        Unavailability unavailability = new Unavailability(period);

        assertTrue(unavailability.isUnavailableIn(slot));
    }


}
