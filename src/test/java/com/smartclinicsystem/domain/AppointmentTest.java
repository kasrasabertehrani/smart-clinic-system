package com.smartclinicsystem.domain;

import com.smartclinicsystem.domain.exception.AppointmentException;
import org.junit.jupiter.api.Test;



import static com.smartclinicsystem.domain.TestFixtures.*;
import static org.junit.jupiter.api.Assertions.*;

public class AppointmentTest {
    @Test
    void testAppointmentStatusOnCreation() {
        Appointment appointment = appointment(patientId("patient-123"), timeSlot(6, 5, 20, 0));
        assertEquals(Appointment.status.SCHEDULED, appointment.getAppointmentStatus());
    }

    @Test
    void testAppointmentInPast() {
       var e = assertThrows(AppointmentException.class, () ->
            appointment(patientId("patient-123"), timeSlot(4,20, 15, 15))
        );
       assertEquals("Cannot schedule an appointment in the past.", e.getMessage());
    }

    @Test
    void testAppointmentMoreThanOneHour() {
      var e =  assertThrows(AppointmentException.class, () ->
            appointment(patientId("patient-123"), timeSlot(6, 20, 15, 0, 90))
        );
      assertEquals("Only 1-hour appointments are supported.", e.getMessage());
    }

    @Test
    void testCancelAppointmentOnScheduledStatus() {
        Appointment appointment = appointment(patientId("patient-123"), 10);
        appointment.cancel(Appointment.CancellationInitiator.PATIENT);
        assertEquals(Appointment.status.CANCELLED, appointment.getAppointmentStatus());
    }

    @Test
    void testCancelAppointmentOnCheckInStatus() {
        Appointment appointment = appointment(patientId("patient-123"), 10);
        appointment.checkIn();
        appointment.cancel(Appointment.CancellationInitiator.PATIENT);
        assertEquals(Appointment.status.CANCELLED, appointment.getAppointmentStatus());
    }

    @Test
    void testCancelAppointmentOnWrongStatus() {
        Appointment appointment = appointment(patientId("patient-123"), 10);
        appointment.checkIn();
        appointment.complete();
        assertThrows(AppointmentException.class, () ->
            appointment.cancel(Appointment.CancellationInitiator.PATIENT)
        );
    }

    @Test
    void testCheckInAppointment() {
        Appointment appointment = appointment(patientId("patient-123"), 10);
        appointment.checkIn();
        assertEquals(Appointment.status.CHECKED_IN, appointment.getAppointmentStatus());
    }

    @Test
    void testCheckInAppointmentOnWrongStatus() {
        Appointment appointment = appointment(patientId("patient-123"), 10);
        appointment.cancel(Appointment.CancellationInitiator.PATIENT);
        assertThrows(AppointmentException.class, appointment::checkIn);
    }

    @Test
    void testCompleteAppointment() {
        Appointment appointment = appointment(patientId("patient-123"), 10);
        appointment.checkIn();
        appointment.complete();
        assertEquals(Appointment.status.COMPLETED, appointment.getAppointmentStatus());
    }

    @Test
    void testCompleteAppointmentOnWrongStatus() {
        Appointment appointment = appointment(patientId("patient-123"), 10);
        appointment.cancel(Appointment.CancellationInitiator.PATIENT);
        assertThrows(AppointmentException.class, appointment::complete);
    }

    @Test
    void testMarkOutAsNoShow() {
        Appointment appointment = appointment(patientId("patient-123"), 10);
        appointment.markAsNoShow();
        assertEquals(Appointment.status.NO_SHOW, appointment.getAppointmentStatus());
    }

    @Test
    void testMarkOutAsNoShowOnWrongStatus() {
        Appointment appointment = appointment(patientId("patient-123"), 10);
        appointment.checkIn();
        appointment.complete();
        assertThrows(AppointmentException.class, appointment::markAsNoShow);
    }

    @Test
    void testAppointmentIsScheduled() {
        Appointment appointment = appointment(patientId("patient-123"), 10);
        assertTrue(appointment.isScheduled());
    }

    @Test
    void testAppointmentIsCanceledByTheSystem() {
        Appointment appointment = appointment(patientId("patient-123"), 10);
        appointment.cancel(Appointment.CancellationInitiator.SYSTEM_AUTOMATION);
        assertTrue(appointment.canceledBySystem());
    }

    @Test
    void testAppointmentIsNotCanceledByTheSystem() {
        Appointment appointment = appointment(patientId("patient-123"), 10);
        assertFalse(appointment.canceledBySystem());
    }

    @Test
    void testAppointmentIsRescheduled() {
        Appointment appointment = appointment(
            patientId("patient-123"),
            timeSlot(6, 5, 20, 15),
            appointmentId("appointment-123")
        );
        assertEquals(appointmentId("appointment-123"), appointment.getRescheduledFromId());
    }

    @Test
    void testPatientHasCheckedIn() {
        Appointment appointment = appointment(patientId("patient-123"), 10);
        appointment.checkIn();
        assertTrue(appointment.hasCheckedIn());
    }

    @Test
    void testPatientHasNotCheckedIn() {
        Appointment appointment = appointment(patientId("patient-123"), 10);
        assertFalse(appointment.hasCheckedIn());
    }

}
