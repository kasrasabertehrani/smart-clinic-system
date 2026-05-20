package com.smartclinicsystem;

import com.smartclinicsystem.domain.exception.InvalidTimePeriodException;
import com.smartclinicsystem.domain.vo.SharpTime;
import com.smartclinicsystem.domain.vo.WorkingShift;
import org.junit.jupiter.api.Test;


import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

public class WorkingShiftTest {
    @Test
    void testNormalWorkingShiftCreation(){
        SharpTime startTime = new SharpTime(LocalTime.of(9, 0));
        SharpTime endTime = new SharpTime(LocalTime.of(17, 0));

        WorkingShift workingShift = new WorkingShift(startTime, endTime);

        assertEquals(workingShift.startTime(), startTime);
        assertEquals(workingShift.endTime(), endTime);
    }
    @Test
    void testWorkingShiftOnStartAfterEnd(){
        SharpTime startTime = new SharpTime(LocalTime.of(17, 0));
        SharpTime endTime = new SharpTime(LocalTime.of(9, 0));


        assertThrows(InvalidTimePeriodException.class, () -> {
            new WorkingShift(startTime, endTime);
        });

    }
    @Test
    void testWorkingShiftOnStartEqualsEnd(){
        SharpTime startTime = new SharpTime(LocalTime.of(9, 0));
        SharpTime endTime = new SharpTime(LocalTime.of(9, 0));
        assertThrows(InvalidTimePeriodException.class, () -> {
            new WorkingShift(startTime, endTime);
        });
    }
    @Test
    void testWorkingShiftOnNullTimeSlot(){
        assertThrows(InvalidTimePeriodException.class, () -> {
            new WorkingShift(null, null);
        });
        assertThrows(InvalidTimePeriodException.class, () -> {
            new WorkingShift(new SharpTime(LocalTime.of(9, 0)), null);
        });
        assertThrows(InvalidTimePeriodException.class, () -> {
            new WorkingShift(null, new SharpTime(LocalTime.of(9, 0)));
        });
    }
    @Test
    void testWorkingShiftCovers(){
        SharpTime startTime = new SharpTime(LocalTime.of(9, 0));
        SharpTime endTime = new SharpTime(LocalTime.of(17, 0));
        WorkingShift workingShift = new WorkingShift(startTime, endTime);

        LocalTime requestedStartTime = LocalTime.of(10, 30);
        LocalTime requestedEndTime = LocalTime.of(12, 0);

        assertTrue(workingShift.covers(requestedStartTime, requestedEndTime));
    }
    @Test
    void testWorkingShiftDoesNotCover(){
        SharpTime startTime = new SharpTime(LocalTime.of(9, 0));
        SharpTime endTime = new SharpTime(LocalTime.of(11, 0));
        WorkingShift workingShift = new WorkingShift(startTime, endTime);

        LocalTime requestedStartTime = LocalTime.of(12, 0);
        LocalTime requestedEndTime = LocalTime.of(13, 0);

        assertFalse(workingShift.covers(requestedStartTime, requestedEndTime));
    }
    @Test
    void testWorkingShiftCoversEqually(){
        SharpTime startTime = new SharpTime(LocalTime.of(11, 0));
        SharpTime endTime = new SharpTime(LocalTime.of(13, 0));
        WorkingShift workingShift = new WorkingShift(startTime, endTime);

        LocalTime requestedStartTime = LocalTime.of(11, 0);
        LocalTime requestedEndTime = LocalTime.of(13, 0);

        assertTrue(workingShift.covers(requestedStartTime, requestedEndTime));
    }
    @Test
    void testWorkingShiftCoversRequestedTimeAfterShiftEnd(){
        SharpTime startTime = new SharpTime(LocalTime.of(9, 0));
        SharpTime endTime = new SharpTime(LocalTime.of(12, 0));
        WorkingShift workingShift = new WorkingShift(startTime, endTime);

        LocalTime requestedStartTime = LocalTime.of(11, 0);
        LocalTime requestedEndTime = LocalTime.of(13, 0);

        assertFalse(workingShift.covers(requestedStartTime, requestedEndTime));
    }
    @Test
    void testWorkingShiftCoversRequestedTimeBeforeShiftStart(){
        SharpTime startTime = new SharpTime(LocalTime.of(12, 0));
        SharpTime endTime = new SharpTime(LocalTime.of(17, 0));
        WorkingShift workingShift = new WorkingShift(startTime, endTime);

        LocalTime requestedStartTime = LocalTime.of(9, 0);
        LocalTime requestedEndTime = LocalTime.of(12, 30);

        assertFalse(workingShift.covers(requestedStartTime, requestedEndTime));
    }
    @Test
    void testWorkingShiftCoversRequestedTimeOnShiftStart(){
        SharpTime startTime = new SharpTime(LocalTime.of(9, 0));
        SharpTime endTime = new SharpTime(LocalTime.of(17, 0));
        WorkingShift workingShift = new WorkingShift(startTime, endTime);

        LocalTime requestedStartTime = LocalTime.of(8, 0);
        LocalTime requestedEndTime = LocalTime.of(9, 0);

        assertFalse(workingShift.covers(requestedStartTime, requestedEndTime));
    }
    @Test
    void testWorkingShiftCoversRequestedTimeOnShiftEnd(){
        SharpTime startTime = new SharpTime(LocalTime.of(9, 0));
        SharpTime endTime = new SharpTime(LocalTime.of(17, 0));
        WorkingShift workingShift = new WorkingShift(startTime, endTime);

        LocalTime requestedStartTime = LocalTime.of(17, 0);
        LocalTime requestedEndTime = LocalTime.of(18, 0);

        assertFalse(workingShift.covers(requestedStartTime, requestedEndTime));
    }

    @Test
    void testWorkingShiftCoversRequestedTimeStartBeforeAndFinishAfterShift(){
        SharpTime startTime = new SharpTime(LocalTime.of(9, 0));
        SharpTime endTime = new SharpTime(LocalTime.of(15, 0));
        WorkingShift workingShift = new WorkingShift(startTime, endTime);

        LocalTime requestedStartTime = LocalTime.of(8, 0);
        LocalTime requestedEndTime = LocalTime.of(17, 0);

        assertFalse(workingShift.covers(requestedStartTime, requestedEndTime));
    }

}
