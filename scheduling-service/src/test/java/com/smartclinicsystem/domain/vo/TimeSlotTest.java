package com.smartclinicsystem.domain.vo;

import com.smartclinicsystem.domain.exception.InvalidTimeSlotException;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static com.smartclinicsystem.domain.TestFixtures.sharpTime;
import static com.smartclinicsystem.domain.TestFixtures.timeSlot;
import static org.junit.jupiter.api.Assertions.*;

public class TimeSlotTest {
    @Test
    public void TimeSlotTestWithNullValues() {
        assertThrows(InvalidTimeSlotException.class, () ->
                new TimeSlot(null, null , null)
        );
        assertThrows(InvalidTimeSlotException.class, () ->
                new TimeSlot(null, null , Duration.ofMinutes(30))
        );
        assertThrows(InvalidTimeSlotException.class, () ->
                new TimeSlot(null, SharpTime.of(9, 0) , null)
        );
        assertThrows(InvalidTimeSlotException.class, () ->
                new TimeSlot(LocalDate.of(2026, 6, 15), null , null)
        );
        assertThrows(InvalidTimeSlotException.class, () ->
                new TimeSlot(LocalDate.of(2026, 6, 15), null , Duration.ofMinutes(30))
        );
        assertThrows(InvalidTimeSlotException.class, () ->
                new TimeSlot(LocalDate.of(2026, 6, 15), SharpTime.of(9, 0) , null)
        );
    }
    @Test
    public void TimeSlotTestWithZeroAndNegativeDuration() {
        assertThrows(InvalidTimeSlotException.class, () ->
                new TimeSlot(LocalDate.of(2026, 6, 15), SharpTime.of(9, 0)
                        , Duration.ZERO)
        );
        assertThrows(InvalidTimeSlotException.class, () ->
                new TimeSlot(LocalDate.of(2026, 6, 15), SharpTime.of(9, 0)
                        , Duration.ofMinutes(-30))
        );
    }
    @Test
    void testTimeSlotOnCreation() {
        LocalDate date = LocalDate.of(2026, 6, 15);
        SharpTime sharpTime = SharpTime.of(9, 0);
        Duration duration = Duration.ofMinutes(30);

        TimeSlot timeSlot = new TimeSlot(date, sharpTime, duration);

        assertEquals(date, timeSlot.date());
        assertEquals(sharpTime, timeSlot.start());
        assertEquals(duration, timeSlot.duration());
    }
    @Test
    void testTimeSlotGetEnd(){
        TimeSlot slot = timeSlot(6, 15, 10, 0, 30);
        assertEquals(sharpTime(10,30), slot.getEnd());
    }
    @Test
    void testTimeSlotGetStartDateTime(){
        TimeSlot slot = timeSlot(6, 15, 10, 0, 30);
        LocalDateTime expected = LocalDateTime.of(2026, 6, 15, 10, 0);
        assertEquals(expected, slot.getStartDateTime());
    }
    @Test
    void testTimeSlotGetEndDateTime(){
        TimeSlot slot = timeSlot(6, 15, 10, 0, 30);
        LocalDateTime expected = LocalDateTime.of(2026, 6, 15, 10, 30);
        assertEquals(expected, slot.getEndDateTime());
    }
    @Test
    void testTimeSlotOverlap(){
        TimeSlot slot = timeSlot(6, 15, 10, 0, 30);
        TimeSlot overlappingSlot = timeSlot(6, 15, 10, 15, 30);

        assertTrue(slot.overlapsWith(overlappingSlot));
    }
    @Test
    void testTimeSlotNotOverlap(){
        TimeSlot slot = timeSlot(6, 15, 10, 0, 30);
        TimeSlot notOverlappingSlot = timeSlot(6, 15, 12, 15, 30);

        assertFalse(slot.overlapsWith(notOverlappingSlot));
    }
    @Test
    void testTimeSlotDoesNotOverlapWithDifferentDate() {
        // Same exact time, but one day apart
        TimeSlot mondaySlot = timeSlot(6, 15, 10, 0, 30);
        TimeSlot tuesdaySlot = timeSlot(6, 16, 10, 0, 30);

        assertFalse(mondaySlot.overlapsWith(tuesdaySlot));
    }

    @Test
    void testTimeSlotDoesNotOverlapWhenEdgesTouch() {
        TimeSlot slot1 = timeSlot(6, 15, 10, 0, 30);  // Ends exactly at 10:30
        TimeSlot slot2 = timeSlot(6, 15, 10, 30, 30); // Starts exactly at 10:30

        assertFalse(slot1.overlapsWith(slot2));
        assertFalse(slot2.overlapsWith(slot1)); // Test commutativity
    }
}
