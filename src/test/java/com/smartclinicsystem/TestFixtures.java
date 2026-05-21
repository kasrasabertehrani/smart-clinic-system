package com.smartclinicsystem;

import com.smartclinicsystem.domain.Appointment;
import com.smartclinicsystem.domain.vo.*;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

/**
 * Factory class for creating common test objects to reduce repetition across tests.
 * This centralizes object creation logic and makes tests more readable.
 */
public class TestFixtures {

    // ==================== TimePeriod Fixtures ====================

    /**
     * Creates a TimePeriod from specific hour values
     */
    public static TimePeriod timePeriod(int year, int month, int day, int startHour, int endHour) {
        LocalDateTime start = LocalDateTime.of(year, month, day, startHour, 0);
        LocalDateTime end = LocalDateTime.of(year, month, day, endHour, 0);
        return new TimePeriod(start, end);
    }

    /**
     * Creates a 1-hour TimePeriod starting at the given hour
     */
    public static TimePeriod timePeriod(int year, int month, int day, int hour) {
        return timePeriod(year, month, day, hour, hour + 1);
    }

    // ==================== SharpTime Fixtures ====================

    /**
     * Creates a SharpTime at the given hour
     */
    public static SharpTime sharpTime(int hour) {
        return SharpTime.of(hour);
    }

    /**
     * Creates a SharpTime from LocalTime
     */
    public static SharpTime sharpTime(int hour, int minute) {
        return new SharpTime(LocalTime.of(hour, minute));
    }

    // ==================== WorkingShift Fixtures ====================

    /**
     * Creates a WorkingShift from hour values
     */
    public static WorkingShift workingShift(int startHour, int endHour) {
        return new WorkingShift(sharpTime(startHour), sharpTime(endHour));
    }

    // ==================== WeeklySchedule Fixtures ====================

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

    /**
     * Creates a WeeklySchedule with specified shifts for specific days
     */
    public static WeeklySchedule weeklySchedule(Map<DayOfWeek, List<WorkingShift>> schedule) {
        return new WeeklySchedule(schedule);
    }

    // ==================== Appointment Fixtures ====================

    /**
     * Creates an Appointment for the given hour on 2026-05-21 (tomorrow, to avoid "past" errors)
     */
    public static Appointment appointment(PatientId patientId, int hour) {
        return new Appointment(patientId, timePeriod(2026, 5, 21, hour));
    }

    /**
     * Creates an Appointment with a custom TimePeriod
     */
    public static Appointment appointment(PatientId patientId, TimePeriod timePeriod) {
        return new Appointment(patientId, timePeriod);
    }

    /**
     * Creates a rescheduled Appointment
     */
    public static Appointment appointment(PatientId patientId, TimePeriod timePeriod, AppointmentId rescheduledFromId) {
        return new Appointment(patientId, timePeriod, rescheduledFromId);
    }

    // ==================== ID Fixtures ====================

    /**
     * Creates a PatientId
     */
    public static PatientId patientId(String id) {
        return new PatientId(id);
    }

    /**
     * Creates an AppointmentId
     */
    public static AppointmentId appointmentId(String id) {
        return new AppointmentId(id);
    }

    /**
     * Creates a DoctorId
     */
    public static DoctorId doctorId(String id) {
        return new DoctorId(id);
    }
}

