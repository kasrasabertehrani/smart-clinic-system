package com.smartclinicsystem.domain.vo;

import com.smartclinicsystem.domain.exception.InvalidTimePeriodException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.LocalTime;

import static com.smartclinicsystem.domain.TestFixtures.*;
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
    @Test
    void testWorkingShiftOverLaps(){
        WorkingShift shift = workingShift(9, 15);
        WorkingShift shift2 = workingShift(10, 16);

        assertTrue(shift.overlapsWith(shift2));
    }
    @Test
    void testWorkingShiftDoesNotOverlap(){
        WorkingShift shift = workingShift(9, 15);
        WorkingShift shift2 = workingShift(16, 17);

        assertFalse(shift.overlapsWith(shift2));
    }
    @Test
    void testWorkingShiftOverlapsEqually(){
        WorkingShift shift = workingShift(9, 15);
        WorkingShift shift2 = workingShift(9, 15);

        assertTrue(shift.overlapsWith(shift2));
    }
    @Test
    void testWorkingShiftDoesNotOverlapOnTwoConsequentWorkingShifts(){
        WorkingShift shift = workingShift(9, 15);
        WorkingShift shift2 = workingShift(15, 17);

        assertFalse(shift.overlapsWith(shift2));
    }
    @Test
    void testWorkingShiftOneContainsOther() {
        WorkingShift large = workingShift(9, 17);
        WorkingShift small = workingShift(10, 15);

        assertTrue(large.overlapsWith(small));
        assertTrue(small.overlapsWith(large)); // Test symmetry
    }

    @Test
    void testWorkingShiftPartialOverlapAtStart() {
        WorkingShift shift1 = workingShift(9, 12);
        WorkingShift shift2 = workingShift(11, 15);

        assertTrue(shift1.overlapsWith(shift2));
        assertTrue(shift2.overlapsWith(shift1)); // Test symmetry
    }

    @Test
    void testWorkingShiftPartialOverlapAtEnd() {
        WorkingShift shift1 = workingShift(14, 17);
        WorkingShift shift2 = workingShift(12, 15);

        assertTrue(shift1.overlapsWith(shift2));
        assertTrue(shift2.overlapsWith(shift1)); // Test symmetry
    }

    @Test
    void testWorkingShiftTouchAtStartBoundary() {
        WorkingShift shift1 = workingShift(9, 12);
        WorkingShift shift2 = workingShift(12, 15);

        assertFalse(shift1.overlapsWith(shift2));
    }

    @Test
    void testWorkingShiftTouchAtEndBoundary() {
        WorkingShift shift1 = workingShift(12, 15);
        WorkingShift shift2 = workingShift(9, 12);

        assertFalse(shift1.overlapsWith(shift2));
    }


}
