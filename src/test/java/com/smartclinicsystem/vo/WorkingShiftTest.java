package com.smartclinicsystem.vo;

import com.smartclinicsystem.domain.exception.InvalidTimePeriodException;
import com.smartclinicsystem.domain.vo.WorkingShift;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.LocalTime;

import static com.smartclinicsystem.TestFixtures.*;
import static org.junit.jupiter.api.Assertions.*;

public class WorkingShiftTest {
    @Test
    void testNormalWorkingShiftCreation() {
        WorkingShift shift = workingShift(9, 17);

        assertEquals(sharpTime(9), shift.startTime());
        assertEquals(sharpTime(17), shift.endTime());
    }

    @Test
    void testWorkingShiftOnStartAfterEnd() {
        assertThrows(InvalidTimePeriodException.class, () ->
            workingShift(17, 9)
        );
    }

    @Test
    void testWorkingShiftOnStartEqualsEnd() {
        assertThrows(InvalidTimePeriodException.class, () ->
            workingShift(9, 9)
        );
    }

    @Test
    void testWorkingShiftOnNullTimeSlot() {
        assertThrows(InvalidTimePeriodException.class, () ->
            new WorkingShift(null, null)
        );
        assertThrows(InvalidTimePeriodException.class, () ->
            new WorkingShift(sharpTime(9), null)
        );
        assertThrows(InvalidTimePeriodException.class, () ->
            new WorkingShift(null, sharpTime(9))
        );
    }

    // ==================== Covers Tests ====================

    @Test
    void testWorkingShiftCoversTimeWithinRange() {
        WorkingShift shift = workingShift(9, 17);

        assertTrue(shift.covers(LocalTime.of(10, 30), LocalTime.of(12, 0)));
    }

    @Test
    void testWorkingShiftDoesNotCoverTimeOutsideRange() {
        WorkingShift shift = workingShift(9, 11);

        assertFalse(shift.covers(LocalTime.of(12, 0), LocalTime.of(13, 0)));
    }

    @Test
    void testWorkingShiftCoversExactBoundaries() {
        WorkingShift shift = workingShift(11, 13);

        assertTrue(shift.covers(LocalTime.of(11, 0), LocalTime.of(13, 0)));
    }

    @ParameterizedTest
    @CsvSource({
        "9,  12,  11, 13, false",   // After shift ends
        "12, 17,  9,  12, false",   // Before shift starts
        "9,  17,  8,  9,  false",   // Ends at shift start
        "9,  17,  17, 18, false"    // Starts at shift end
    })
    void testWorkingShiftBoundaryConditions(int shiftStart, int shiftEnd, int reqStart, int reqEnd, boolean expected) {
        WorkingShift shift = workingShift(shiftStart, shiftEnd);

        assertEquals(expected, shift.covers(LocalTime.of(reqStart, 0), LocalTime.of(reqEnd, 0)));
    }

    @Test
    void testWorkingShiftCoversTimeSpanningShift() {
        WorkingShift shift = workingShift(9, 15);

        assertFalse(shift.covers(LocalTime.of(8, 0), LocalTime.of(17, 0)));
    }

}
