package com.smartclinicsystem.domain;

import com.smartclinicsystem.domain.vo.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Factory class for creating common test objects to reduce repetition across tests.
 * This centralizes object creation logic and makes tests more readable.
 */
public class TestFixtures {

    public static TimePeriod timePeriod(int year, int month, int day, int startHour, int endHour) {
        LocalDateTime start = LocalDateTime.of(year, month, day, startHour, 0);
        LocalDateTime end = LocalDateTime.of(year, month, day, endHour, 0);
        return new TimePeriod(start, end);
    }


    public static TimePeriod timePeriod(int year, int month, int day, int hour) {
        return timePeriod(year, month, day, hour, hour + 1);
    }


    public static SharpTime sharpTime(int hour) {
        return SharpTime.of(hour);
    }




    public static WorkingShift workingShift(int startHour, int endHour) {
        return new WorkingShift(sharpTime(startHour), sharpTime(endHour));
    }



    /**
     * Creates a standard weekly schedule for testing:
     * - Monday & Tuesday: 9 AM to 5 PM
     * - Wednesday: 8 AM to 12 PM
     * - Thursday: Split shift (9 AM - 12 PM, 1 PM - 6 PM)
     * - Friday: 10 AM to 4 PM
     * - Saturday & Sunday: Closed
     */
    public static WeeklySchedule standardWeeklySchedule() {
        Map<DayOfWeek, List<WorkingShift>> weeklyMap = new EnumMap<>(DayOfWeek.class);

        weeklyMap.put(DayOfWeek.MONDAY, List.of(workingShift(9, 17)));
        weeklyMap.put(DayOfWeek.TUESDAY, List.of(workingShift(9, 17)));
        weeklyMap.put(DayOfWeek.WEDNESDAY, List.of(workingShift(8, 12)));
        weeklyMap.put(DayOfWeek.THURSDAY, List.of(
                workingShift(9, 12),
                workingShift(13, 18)
        ));
        weeklyMap.put(DayOfWeek.FRIDAY, List.of(workingShift(10, 16)));
        weeklyMap.put(DayOfWeek.SATURDAY, Collections.emptyList());
        weeklyMap.put(DayOfWeek.SUNDAY, Collections.emptyList());

        return new WeeklySchedule(weeklyMap);
    }


    public static WeeklySchedule weeklySchedule(Map<DayOfWeek, List<WorkingShift>> schedule) {
        return new WeeklySchedule(schedule);
    }



    public static Appointment appointment(PatientId patientId, int hour) {
        return new Appointment(patientId, timePeriod(2026, 6, 21, hour));
    }


    public static Appointment appointment(PatientId patientId, TimePeriod timePeriod) {
        return new Appointment(patientId, timePeriod);
    }

    public static Appointment appointment(PatientId patientId, TimePeriod timePeriod, AppointmentId rescheduledFromId) {
        return new Appointment(patientId, timePeriod, rescheduledFromId);
    }


    public static PatientId patientId(String id) {
        return new PatientId(id);
    }


    public static AppointmentId appointmentId(String id) {
        return new AppointmentId(id);
    }


    public static DoctorId doctorId(String id) {
        return new DoctorId(id);
    }



    public static EffectiveSchedule effectiveSchedule(int year, int month, int day, WeeklySchedule schedule) {
        return new EffectiveSchedule(LocalDate.of(year, month, day), schedule);
    }

    public static class WeeklyScheduleBuilder {
        private final Map<DayOfWeek, List<WorkingShift>> map = new EnumMap<>(DayOfWeek.class);

        public WeeklyScheduleBuilder() {
            for (DayOfWeek day : DayOfWeek.values()) {
                map.put(day, new ArrayList<>());
            }
        }

        public WeeklyScheduleBuilder withStandardWeekdays(int startHour, int endHour) {
            WorkingShift shift = workingShift(startHour, endHour);
            map.get(DayOfWeek.MONDAY).add(shift);
            map.get(DayOfWeek.TUESDAY).add(shift);
            map.get(DayOfWeek.WEDNESDAY).add(shift);
            map.get(DayOfWeek.THURSDAY).add(shift);
            map.get(DayOfWeek.FRIDAY).add(shift);
            return this;
        }

        public WeeklyScheduleBuilder withShift(DayOfWeek day, int startHour, int endHour) {
            map.get(day).add(workingShift(startHour, endHour));
            return this;
        }

        public WeeklySchedule build() {
            return new WeeklySchedule(map);
        }
        public WeeklyScheduleBuilder overrideDay(DayOfWeek day, int startHour, int endHour) {
            map.get(day).clear();
            map.get(day).add(workingShift(startHour, endHour));
            return this;
        }
    }

    public static WeeklyScheduleBuilder scheduleBuilder() {
        return new WeeklyScheduleBuilder();
    }
}

