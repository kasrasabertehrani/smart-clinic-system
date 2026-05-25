package com.smartclinicsystem.domain;

import com.smartclinicsystem.domain.vo.*;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;



import static com.smartclinicsystem.domain.TestFixtures.*;

/**
 * High-level factory class specifically for AppointmentCalendar scenarios.
 * Relies on the base TestFixtures class for primitive domain objects.
 */
public class CalendarFixtures {



    public static EffectiveSchedule customEffectiveSchedule() {
        WeeklySchedule weeklyMap = standardWeeklySchedule();
        return effectiveSchedule(2026, 6, 1, weeklySchedule(weeklyMap.schedule()));
    }




    public static Unavailability customUnavailability() {
        LocalDateTime start = LocalDateTime.of(2026, 6, 10, 9, 0);
        LocalDateTime end = LocalDateTime.of(2026, 6, 12, 17, 0);

        // Bypass the helper method so we don't lose the end date!
        return new Unavailability(
                new TimePeriod(start, end)
        );
    }



    public static AppointmentCalendar customCalendar(DoctorId doctorId) {

        List<EffectiveSchedule> schedules = new ArrayList<>(List.of(
                customEffectiveSchedule()
        ));

        List<Unavailability> unavailabilities = new ArrayList<>(List.of(
                customUnavailability()
        ));

        List<Appointment> existingAppointments = new ArrayList<>(List.of(
                appointment(patientId("pat-june-1"), timePeriod(2026, 6, 2, 10)),
                appointment(patientId("pat-june-2"), timePeriod(2026, 6, 4, 14))
        ));

        return new AppointmentCalendar(
                doctorId,
                schedules,
                unavailabilities,
                existingAppointments
        );
    }
}