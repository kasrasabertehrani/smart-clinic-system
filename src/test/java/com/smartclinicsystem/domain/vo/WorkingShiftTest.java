package com.smartclinicsystem.domain.vo;

import com.smartclinicsystem.domain.exception.InvalidTimePeriodException;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;

import static com.smartclinicsystem.domain.TestFixtures.*;
import static org.junit.jupiter.api.Assertions.*;

public class WorkingShiftTest {
    @Test
    void testNormalWorkingShiftCreation() {
        WorkingShift shift = workingShift(9,15, 17,30);

        assertEquals(sharpTime(9, 15), shift.startTime());
        assertEquals(sharpTime(17, 30), shift.endTime());
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
            new WorkingShift(sharpTime(9, 0), null)
        );
        assertThrows(InvalidTimePeriodException.class, () ->
            new WorkingShift(null, sharpTime(9, 0)
        ));
    }


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


    @Test
    void testWorkingShiftCoversTheRequestedTimeWithPartialOverlap() {
        WorkingShift shift = workingShift(9, 15, 17, 30);

        assertFalse(shift.covers(LocalTime.of(8, 0), LocalTime.of(12, 30)));
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
