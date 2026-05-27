package com.smartclinicsystem.domain;

import com.smartclinicsystem.domain.exception.BookingException;
import com.smartclinicsystem.domain.exception.InvalidEffectiveScheduleException;
import com.smartclinicsystem.domain.exception.AppointmentNotFoundException;
import com.smartclinicsystem.domain.vo.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static com.smartclinicsystem.domain.TestFixtures.*;

public class AppointmentCalendarTest {

    private AppointmentCalendar calendar;
    @BeforeEach
    void setUp() {
        this.calendar = CalendarFixtures.customCalendar(TestFixtures.doctorId("doc-1"));
    }
    @Test
    void testBaseCalendarPropertiesExist() {
        assertNotNull(calendar.getAppointments());
        assertNotNull(calendar.getDoctorId());
        assertNotNull(calendar.getEffectiveSchedules());
        assertNotNull(calendar.getUnavailabilities());
    }
    @Test
    void testValidateBookingRulesWhenDoctorIsUnavailable(){
        TimeSlot slot = timeSlot(6, 11, 10, 15);

        var e = assertThrows(BookingException.class, () -> calendar.validateBookingRules(slot));

        assertEquals("Cannot book: The doctor is on leave during this time.", e.getMessage());
    }
    @Test
    void testValidateBookingRulesWhenAppointmentIsAlreadyBooked(){
        TimeSlot slot = timeSlot(6, 2, 10, 0);

        var e = assertThrows(BookingException.class, () -> calendar.validateBookingRules(slot));

        assertEquals("Cannot book: This time slot is already taken.", e.getMessage());
    }
    @Test
    void testValidateBookingRulesWhenDoctorIsNotWorking(){
        TimeSlot slot = timeSlot(6, 7, 10, 0);

        var e = assertThrows(BookingException.class, () -> calendar.validateBookingRules(slot));

        assertEquals("Cannot book: The requested time falls outside working hours.", e.getMessage());
    }
    @Test
    void testValidateBookingRulesWhenAllConditionsAreMet(){
        TimeSlot slot = timeSlot(6, 3, 10, 0);

        assertDoesNotThrow(() -> calendar.validateBookingRules(slot));
    }
    @Test
    void testAddAppointment(){
        PatientId patientId = patientId("pat-1");
        TimeSlot slot = timeSlot(6, 3, 10, 0);

        calendar.addAppointment(patientId, slot);

        assertEquals(3, calendar.getAppointments().size());
    }
    @Test
    void testCancelAppointment() {
        AppointmentId appointmentId = calendar.getAppointments().get(1).getId();
        Appointment.CancellationInitiator initiator = Appointment.CancellationInitiator.CLINIC_RECEPTION;

        calendar.cancelAppointment(appointmentId, initiator);

        assertSame(Appointment.status.CANCELLED, calendar.getAppointments().get(1).getAppointmentStatus());
    }
    @Test
    void testCheckInPatient() {
        AppointmentId appointmentId = calendar.getAppointments().get(1).getId();

        calendar.checkInPatient(appointmentId);

        assertSame(Appointment.status.CHECKED_IN, calendar.getAppointments().get(1).getAppointmentStatus());
    }
    @Test
    void testCompleteAppointment() {
        AppointmentId appointmentId = calendar.getAppointments().get(1).getId();
        calendar.checkInPatient(appointmentId);

        calendar.completeAppointment(appointmentId);

        assertSame(Appointment.status.COMPLETED, calendar.getAppointments().get(1).getAppointmentStatus());
    }
    @Test
    void testMarkNoShow() {
        AppointmentId appointmentId = calendar.getAppointments().get(1).getId();

        calendar.recordNoShow(appointmentId);

        assertSame(Appointment.status.NO_SHOW, calendar.getAppointments().get(1).getAppointmentStatus());
    }
    @Test
    void testRescheduleActiveAppointment() {
        AppointmentId oldAppointmentId = calendar.getAppointments().get(1).getId();
        Appointment.CancellationInitiator initiator = Appointment.CancellationInitiator.PATIENT;
        TimeSlot newTimeSlot = timeSlot(6, 9, 10, 15);

        calendar.rescheduleActiveAppointment(oldAppointmentId, newTimeSlot, initiator);

        assertEquals(3, calendar.getAppointments().size());
        assertNotNull(calendar.getAppointments().get(2).getRescheduledFromId());
    }
    @Test
    void testRescheduleActiveAppointmentWhenAppointmentIsAlreadyCompleted() {
        AppointmentId oldAppointmentId = calendar.getAppointments().get(1).getId();
        calendar.checkInPatient(oldAppointmentId);
        calendar.completeAppointment(oldAppointmentId);

        TimeSlot newTimeSlot = timeSlot(6, 9, 10, 15);
        Appointment.CancellationInitiator initiator = Appointment.CancellationInitiator.PATIENT;

        var e = assertThrows(BookingException.class, () ->
                calendar.rescheduleActiveAppointment(oldAppointmentId, newTimeSlot, initiator)
        );

        assertEquals("Only active appointments can be actively rescheduled.", e.getMessage());
    }
    @Test
    void testRescheduleActiveAppointmentWhenCheckedIn() {
        AppointmentId oldAppointmentId = calendar.getAppointments().get(1).getId();
        calendar.checkInPatient(oldAppointmentId);

        TimeSlot newTimeSlot = timeSlot(6, 9, 10, 15);
        Appointment.CancellationInitiator initiator = Appointment.CancellationInitiator.PATIENT;

        calendar.rescheduleActiveAppointment(oldAppointmentId, newTimeSlot, initiator);

        assertEquals(3, calendar.getAppointments().size());
        assertNotNull(calendar.getAppointments().get(2).getRescheduledFromId());
    }
    @Test
    void testRescheduleSystemCancelledAppointment() {
        AppointmentId oldAppointmentId = calendar.getAppointments().get(1).getId();
        calendar.cancelAppointment(oldAppointmentId, Appointment.CancellationInitiator.SYSTEM_AUTOMATION);
        TimeSlot newTimeSlot = timeSlot(6, 9, 10, 15);

        calendar.rescheduleSystemCancelledAppointment(oldAppointmentId, newTimeSlot);

        assertEquals(3, calendar.getAppointments().size());
        assertNotNull(calendar.getAppointments().get(2).getRescheduledFromId());
    }
    @Test
    void testRescheduleSystemCancelledAppointmentWhenAppointmentIsNotCancelledBySystem() {
        AppointmentId oldAppointmentId = calendar.getAppointments().get(1).getId();
        calendar.cancelAppointment(oldAppointmentId, Appointment.CancellationInitiator.PATIENT);
        TimeSlot newTimeSlot = timeSlot(6, 9, 10, 15);


        var e = assertThrows(BookingException.class, () ->
                calendar.rescheduleSystemCancelledAppointment(oldAppointmentId, newTimeSlot)
        );
        assertEquals("This method is only for replacing appointments that the system previously cancelled.",
                e.getMessage());

    }
    @Test
    void testAddUnavailabilityWithoutEffectingExistingAppointments() {
        LocalDateTime start = LocalDateTime.of(2026, 6, 15, 9, 0);
        LocalDateTime end = LocalDateTime.of(2026, 6, 19, 10, 0);
        TimePeriod leaveTimePeriod = new TimePeriod(start, end);

        calendar.addUnavailability(leaveTimePeriod);

        assertEquals(2, calendar.getUnavailabilities().size());
    }
    @Test
    void testAddUnavailabilityWithEffectingExistingAppointments() {
        LocalDateTime start = LocalDateTime.of(2026, 6, 1, 9, 0);
        LocalDateTime end = LocalDateTime.of(2026, 6, 5, 10, 0);
        TimePeriod leaveTimePeriod = new TimePeriod(start, end);
        calendar.addUnavailability(leaveTimePeriod);

        for (Appointment appointment : calendar.getAppointments()) {
            assertEquals(Appointment.status.CANCELLED, appointment.getAppointmentStatus());
        }
    }
    @Test
    void testChangeScheduleWithoutEffectingExistingAppointments() {
        WeeklySchedule newSchedule = TestFixtures.scheduleBuilder()
                .withStandardWeekdays(9, 15, 17, 30)
                .withShift(DayOfWeek.SATURDAY, 10, 0, 14, 0)
                .build();

        LocalDate validFrom = LocalDate.of(2026, 7, 15);

        calendar.changeSchedule(newSchedule, validFrom);

        for (Appointment appointment : calendar.getAppointments()) {
            assertEquals(Appointment.status.SCHEDULED, appointment.getAppointmentStatus());
        }

    }
    @Test
    void testChangeScheduleWithTheSameValidFrom() {
        WeeklySchedule newSchedule = TestFixtures.scheduleBuilder()
                .withStandardWeekdays(15, 15, 17, 30)
                .withShift(DayOfWeek.SATURDAY, 10, 0, 14, 0)
                .build();
        LocalDate validFrom = LocalDate.of(2026, 6, 1);

        var e = assertThrows(InvalidEffectiveScheduleException.class, () ->
                calendar.changeSchedule(newSchedule, validFrom));

        assertEquals("A schedule already exists starting on " + validFrom +
                ". You must edit the existing schedule or choose a different start date.", e.getMessage());
    }
    @Test
    void testChangeScheduleWithEffectingExistingAppointments() {
        WeeklySchedule newSchedule = TestFixtures.scheduleBuilder()
                .withStandardWeekdays(15, 15, 17, 30)
                .withShift(DayOfWeek.SATURDAY, 10, 0, 14, 0)
                .build();
        LocalDate validFrom = LocalDate.of(2026, 6, 2);

        calendar.changeSchedule(newSchedule, validFrom);

        for (Appointment appointment : calendar.getAppointments()) {
            assertEquals(Appointment.status.CANCELLED, appointment.getAppointmentStatus());
        }
    }
    @Test
    void testFindAppointmentOrThrowWithInvalidId() {
        AppointmentId invalidId = new AppointmentId("invalid-id");
        var e = assertThrows(AppointmentNotFoundException.class, () -> calendar.findAppointmentOrThrow(invalidId));
        assertEquals("Appointment with ID " + invalidId.value() + " was not found in this calendar.",
                e.getMessage());
    }

}
