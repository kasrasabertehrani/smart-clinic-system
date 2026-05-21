package com.smartclinicsystem;

import com.smartclinicsystem.domain.exception.InvalidEffectiveScheduleException;
import com.smartclinicsystem.domain.vo.EffectiveSchedule;
import com.smartclinicsystem.domain.vo.WeeklySchedule;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static com.smartclinicsystem.TestFixtures.standardWeeklySchedule;
import static org.junit.jupiter.api.Assertions.*;


public class EffectiveScheduleTest {
    @Test
    void testEffectiveScheduleCreation() {
        WeeklySchedule schedule = standardWeeklySchedule();
        LocalDate validFrom = LocalDate.of(2026, 5, 20);

        EffectiveSchedule effectiveSchedule = new EffectiveSchedule(validFrom, schedule);

        assertEquals(validFrom, effectiveSchedule.validFrom());
        assertEquals(schedule, effectiveSchedule.schedule());
    }
    @Test
    void testEffectiveScheduleCreationWithNullSchedule() {
        LocalDate validFrom = LocalDate.of(2026, 5, 20);

        assertThrows(InvalidEffectiveScheduleException.class, () -> new EffectiveSchedule(validFrom, null));
    }
    @Test
    void testEffectiveScheduleCreationWithNullValidFrom() {
        WeeklySchedule schedule = standardWeeklySchedule();

        assertThrows(InvalidEffectiveScheduleException.class, () -> new EffectiveSchedule(null, schedule));
    }
    @Test
    void testAppliesToAfterValidFrom() {
        WeeklySchedule schedule = standardWeeklySchedule();
        LocalDate validFrom = LocalDate.of(2026, 5, 20);
        LocalDate date = LocalDate.of(2026, 5, 21);
        EffectiveSchedule effectiveSchedule = new EffectiveSchedule(validFrom, schedule);

        assertTrue(effectiveSchedule.appliesTo(date));
    }
    @Test
    void testAppliesToBeforeValidFrom() {
        WeeklySchedule schedule = standardWeeklySchedule();
        LocalDate validFrom = LocalDate.of(2026, 5, 20);
        LocalDate date = LocalDate.of(2026, 5, 19);
        EffectiveSchedule effectiveSchedule = new EffectiveSchedule(validFrom, schedule);

        assertFalse(effectiveSchedule.appliesTo(date));
    }
}
