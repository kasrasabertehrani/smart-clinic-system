package com.smartclinicsystem;

import com.smartclinicsystem.domain.Appointment;
import com.smartclinicsystem.domain.AppointmentCalendar;
import com.smartclinicsystem.domain.exception.BookingException;
import com.smartclinicsystem.domain.exception.InvalidEffectiveScheduleException;
import com.smartclinicsystem.domain.exception.AppointmentNotFoundException;
import com.smartclinicsystem.domain.vo.AppointmentId;
import com.smartclinicsystem.domain.vo.PatientId;
import com.smartclinicsystem.domain.vo.TimePeriod;
import com.smartclinicsystem.domain.vo.WeeklySchedule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static com.smartclinicsystem.TestFixtures.*;

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
        TimePeriod timePeriod = timePeriod(2026, 6, 11,10);

        var e = assertThrows(BookingException.class, () -> calendar.validateBookingRules(timePeriod));
        assertEquals("Cannot book: The doctor is on leave during this time.", e.getMessage());
    }
    @Test
    void testValidateBookingRulesTimeSlotIsUnavailable(){
        TimePeriod timePeriod = timePeriod(2026, 6, 2,10);

        var e = assertThrows(BookingException.class, () -> calendar.validateBookingRules(timePeriod));
        assertEquals("Cannot book: This time slot is already taken.", e.getMessage());
    }
    @Test
    void testValidateBookingRulesWhenDoctorIsNotWorking(){
        TimePeriod timePeriod = timePeriod(2026, 6, 14,10);

        var e = assertThrows(BookingException.class, () -> calendar.validateBookingRules(timePeriod));
        assertEquals("Cannot book: The requested time falls outside working hours.", e.getMessage());
    }
    @Test
    void testValidateBookingRulesWhenAllConditionsAreMet(){
        TimePeriod timePeriod = timePeriod(2026, 6, 15,10);

        assertDoesNotThrow(() -> calendar.validateBookingRules(timePeriod));
    }
    @Test
    void testAddAppointment(){
        TimePeriod timePeriod = timePeriod(2026, 6, 15,10);
        PatientId patientId = patientId("pat-1");

        calendar.addAppointment(patientId, timePeriod);
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

        calendar.checkInPatient(appointmentId);
        calendar.recordNoShow(appointmentId);

        assertSame(Appointment.status.NO_SHOW, calendar.getAppointments().get(1).getAppointmentStatus());
    }
    @Test
    void testRescheduleActiveAppointment() {
        AppointmentId appointmentId = calendar.getAppointments().get(1).getId();
        TimePeriod newTimePeriod = timePeriod(2026, 6, 15,11);
        Appointment.CancellationInitiator initiator = Appointment.CancellationInitiator.CLINIC_RECEPTION;

        calendar.rescheduleActiveAppointment(appointmentId, newTimePeriod, initiator);

        assertEquals(newTimePeriod, calendar.getAppointments().get(2).getTimePeriod());
        assertSame(Appointment.status.CANCELLED, calendar.getAppointments().get(1).getAppointmentStatus());
    }
    @Test
    void testRescheduleActiveAppointmentWhenAppointmentIsAlreadyCompleted() {
        AppointmentId appointmentId = calendar.getAppointments().get(1).getId();
        Appointment.CancellationInitiator initiator = Appointment.CancellationInitiator.CLINIC_RECEPTION;
        TimePeriod newTimePeriod = timePeriod(2026, 6, 15,11);
        calendar.cancelAppointment(appointmentId, initiator);

        var e = assertThrows(BookingException.class, () -> calendar.rescheduleActiveAppointment(
                appointmentId, newTimePeriod, initiator));
        assertEquals("Only active appointments can be actively rescheduled.", e.getMessage());

    }
    @Test
    void testRescheduleActiveAppointmentWhenCheckedIn() {
        AppointmentId appointmentId = calendar.getAppointments().get(1).getId();
        Appointment.CancellationInitiator initiator = Appointment.CancellationInitiator.CLINIC_RECEPTION;
        TimePeriod newTimePeriod = timePeriod(2026, 6, 15,11);
        calendar.checkInPatient(appointmentId);
        calendar.rescheduleActiveAppointment(appointmentId, newTimePeriod, initiator);

        assertEquals(newTimePeriod, calendar.getAppointments().get(2).getTimePeriod());
        assertSame(Appointment.status.CANCELLED, calendar.getAppointments().get(1).getAppointmentStatus());

    }
    @Test
    void testRescheduleSystemCancelledAppointment() {
        AppointmentId appointmentId = calendar.getAppointments().get(1).getId();
        TimePeriod newTimePeriod = timePeriod(2026, 6, 15,11);
        calendar.cancelAppointment(appointmentId, Appointment.CancellationInitiator.SYSTEM_AUTOMATION);

        calendar.rescheduleSystemCancelledAppointment(appointmentId, newTimePeriod);

        assertEquals(newTimePeriod, calendar.getAppointments().get(2).getTimePeriod());

    }
    @Test
    void testRescheduleSystemCancelledAppointmentWhenAppointmentIsNotCancelledBySystem() {
        AppointmentId appointmentId = calendar.getAppointments().get(1).getId();
        TimePeriod newTimePeriod = timePeriod(2026, 6, 15,11);
        calendar.cancelAppointment(appointmentId, Appointment.CancellationInitiator.PATIENT);

        var e = assertThrows(BookingException.class, () -> calendar.rescheduleSystemCancelledAppointment(
                appointmentId, newTimePeriod));
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
        WeeklySchedule newWeeklySchedule = TestFixtures.scheduleBuilder()
                .withStandardWeekdays(9, 17)
                .withShift(DayOfWeek.WEDNESDAY, 9, 12)
                .build();
        LocalDate validFrom = LocalDate.of(2026, 7, 1);

        calendar.changeSchedule(newWeeklySchedule, validFrom);
        assertEquals(2, calendar.getEffectiveSchedules().size());
    }
    @Test
    void testChangeScheduleWithTheSameValidFrom() {
        WeeklySchedule newWeeklySchedule = TestFixtures.scheduleBuilder()
                .withStandardWeekdays(9, 17)
                .withShift(DayOfWeek.WEDNESDAY, 9, 12)
                .build();
        LocalDate validFrom = LocalDate.of(2026, 6, 1);

        var e = assertThrows(InvalidEffectiveScheduleException.class, () -> calendar.changeSchedule(
                newWeeklySchedule, validFrom));
        assertEquals("A schedule already exists starting on " + validFrom +
                ". You must edit the existing schedule or choose a different start date.", e.getMessage());
    }
    @Test
    void testChangeScheduleWithEffectingExistingAppointments() {
        WeeklySchedule newWeeklySchedule = TestFixtures.scheduleBuilder()
                .withStandardWeekdays(9, 17)
                .overrideDay(DayOfWeek.TUESDAY, 7, 10)
                .overrideDay(DayOfWeek.THURSDAY, 9, 12)
                .build();
        LocalDate validFrom = LocalDate.of(2026, 6, 2);
        calendar.changeSchedule(newWeeklySchedule, validFrom);

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
