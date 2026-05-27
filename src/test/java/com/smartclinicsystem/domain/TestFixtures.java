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
    public static TimePeriod timePeriod(int startMonth, int startDay, int startHour,
                                        int endMonth, int endDay,  int endHour){
        LocalDateTime start = LocalDateTime.of(2026, startMonth, startDay, startHour, 15);
        LocalDateTime end = LocalDateTime.of(2026, endMonth, endDay, endHour, 30);
        return new TimePeriod(start, end);
    }


    public static TimePeriod timePeriod(int year, int month, int day, int hour) {
        return timePeriod(year, month, day, hour, hour + 1);
    }

    public static TimeSlot timeSlot(int month, int day, int hour, int minute) {
        return new TimeSlot(LocalDate.of(2026, month, day),
                sharpTime(hour, minute), java.time.Duration.ofHours(1));
    }
    public static TimeSlot timeSlot(int month, int day, int hour, int minute, int durationInMinutes) {
        return new TimeSlot(LocalDate.of(2026, month, day),
                sharpTime(hour, minute), java.time.Duration.ofMinutes(durationInMinutes));
    }


    public static SharpTime sharpTime(int hour, int minute) {
        return SharpTime.of(hour, minute);
    }




    public static WorkingShift workingShift(int startHour, int endHour) {
        return new WorkingShift(sharpTime(startHour, 0), sharpTime(endHour, 0));
    }
    public static WorkingShift workingShift(int startHour, int startMinute, int endHour, int endMinute) {
        return new WorkingShift(sharpTime(startHour, startMinute), sharpTime(endHour, endMinute));
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

        weeklyMap.put(DayOfWeek.MONDAY, List.of(workingShift(9,15, 17,30)));
        weeklyMap.put(DayOfWeek.TUESDAY, List.of(workingShift(9, 15, 17, 30)));
        weeklyMap.put(DayOfWeek.WEDNESDAY, List.of(workingShift(8,30, 12,30)));
        weeklyMap.put(DayOfWeek.THURSDAY, List.of(
                workingShift(9, 12),
                workingShift(13, 18)
        ));
        weeklyMap.put(DayOfWeek.FRIDAY, List.of(workingShift(10,45 , 16,15)));
        weeklyMap.put(DayOfWeek.SATURDAY, Collections.emptyList());
        weeklyMap.put(DayOfWeek.SUNDAY, Collections.emptyList());

        return new WeeklySchedule(weeklyMap);
    }


    public static WeeklySchedule weeklySchedule(Map<DayOfWeek, List<WorkingShift>> schedule) {
        return new WeeklySchedule(schedule);
    }



    public static Appointment appointment(PatientId patientId, int hour) {
        return new Appointment(patientId, timeSlot(6, 20, hour, 0));
    }

    public static Appointment appointment(PatientId patientId, TimeSlot timeSlot) {
        return new Appointment(patientId, timeSlot);
    }


    public static Appointment appointment(PatientId patientId, TimeSlot timeSlot, AppointmentId rescheduledFromId) {
        return new Appointment(patientId, timeSlot, rescheduledFromId);
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

        public WeeklyScheduleBuilder withStandardWeekdays(int startHour,int startMinute, int endHour, int endMinute) {
            WorkingShift shift = workingShift(startHour, startMinute, endHour, endMinute);
            map.get(DayOfWeek.MONDAY).add(shift);
            map.get(DayOfWeek.TUESDAY).add(shift);
            map.get(DayOfWeek.WEDNESDAY).add(shift);
            map.get(DayOfWeek.THURSDAY).add(shift);
            map.get(DayOfWeek.FRIDAY).add(shift);
            return this;
        }

        public WeeklyScheduleBuilder withShift(DayOfWeek day, int startHour,int startMinute, int endHour
                , int endMinute) {
            map.get(day).add(workingShift(startHour, startMinute, endHour, endMinute));
            return this;
        }

        public WeeklyScheduleBuilder withNullShiftList(DayOfWeek day) {
            map.put(day, null);
            return this;
        }

        public WeeklySchedule build() {
            return new WeeklySchedule(map);
        }
    }

    public static WeeklyScheduleBuilder scheduleBuilder() {
        return new WeeklyScheduleBuilder();
    }
}

