package com.smartclinicsystem;

import com.smartclinicsystem.domain.Appointment;
import com.smartclinicsystem.domain.exception.AppointmentException;
import com.smartclinicsystem.domain.vo.AppointmentId;
import com.smartclinicsystem.domain.vo.PatientId;
import com.smartclinicsystem.domain.vo.TimePeriod;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class AppointmentTest {
    @Test
    void testAppointmentStatusOnCreation() {
        PatientId patientId = new PatientId("patient-123");
        LocalDateTime startTime = LocalDateTime.of(2026, 5, 20, 15, 0);
        LocalDateTime endTime = LocalDateTime.of(2026, 5, 20, 16, 0);
        TimePeriod timePeriod = new TimePeriod(startTime, endTime);

        Appointment appointment = new Appointment(patientId ,timePeriod);

        assertEquals(Appointment.status.SCHEDULED, appointment.getAppointmentStatus());
    }
    @Test
    void testAppointmentInPast(){
        PatientId patientId = new PatientId("patient-123");
        LocalDateTime startTime = LocalDateTime.of(2026, 5, 16, 15, 0);
        LocalDateTime endTime = LocalDateTime.of(2026, 5, 16, 16, 0);
        TimePeriod timePeriod = new TimePeriod(startTime, endTime);

        assertThrows(AppointmentException.class, () -> {
            new Appointment(patientId, timePeriod);
        });
    }
    @Test
    void testAppointmentMoreThanOneHour(){
        PatientId patientId = new PatientId("patient-123");
        LocalDateTime startTime = LocalDateTime.of(2026, 5, 20, 10, 0);
        LocalDateTime endTime = LocalDateTime.of(2026, 5, 20, 12, 0);
        TimePeriod timePeriod = new TimePeriod(startTime, endTime);

        assertThrows(AppointmentException.class, () -> {
            new Appointment(patientId, timePeriod);
        });
    }
    @Test
    void testCancelAppointmentOnScheduledStatus(){
        PatientId patientId = new PatientId("patient-123");
        LocalDateTime startTime = LocalDateTime.of(2026, 5, 20, 10, 0);
        LocalDateTime endTime = LocalDateTime.of(2026, 5, 20, 11, 0);
        TimePeriod timePeriod = new TimePeriod(startTime, endTime);
        Appointment appointment = new Appointment(patientId, timePeriod);

        appointment.cancel(Appointment.CancellationInitiator.PATIENT);

        assertEquals(Appointment.status.CANCELLED, appointment.getAppointmentStatus());
    }

    @Test
    void testCancelAppointmentOnCheckInStatus(){
        PatientId patientId = new PatientId("patient-123");
        LocalDateTime startTime = LocalDateTime.of(2026, 5, 20, 10, 0);
        LocalDateTime endTime = LocalDateTime.of(2026, 5, 20, 11, 0);
        TimePeriod timePeriod = new TimePeriod(startTime, endTime);
        Appointment appointment = new Appointment(patientId, timePeriod);

        appointment.checkIn();
        appointment.cancel(Appointment.CancellationInitiator.PATIENT);

        assertEquals(Appointment.status.CANCELLED, appointment.getAppointmentStatus());
    }

    @Test
    void testCancelAppointmentOnWrongStatus(){
        PatientId patientId = new PatientId("patient-123");
        LocalDateTime startTime = LocalDateTime.of(2026, 5, 20, 10, 0);
        LocalDateTime endTime = LocalDateTime.of(2026, 5, 20, 11, 0);
        TimePeriod timePeriod = new TimePeriod(startTime, endTime);
        Appointment appointment = new Appointment(patientId, timePeriod);
        appointment.checkIn();
        appointment.complete();
        assertThrows(AppointmentException.class, () -> {
            appointment.cancel(Appointment.CancellationInitiator.PATIENT);
        });
    }

    @Test
    void testCheckInAppointment(){
        PatientId patientId = new PatientId("patient-123");
        LocalDateTime startTime = LocalDateTime.of(2026, 5, 20, 10, 0);
        LocalDateTime endTime = LocalDateTime.of(2026, 5, 20, 11, 0);
        TimePeriod timePeriod = new TimePeriod(startTime, endTime);
        Appointment appointment = new Appointment(patientId, timePeriod);

        appointment.checkIn();

        assertEquals(Appointment.status.CHECKED_IN, appointment.getAppointmentStatus());
    }
    @Test
    void testCheckInAppointmentOnWrongStatus(){
        PatientId patientId = new PatientId("patient-123");
        LocalDateTime startTime = LocalDateTime.of(2026, 5, 20, 10, 0);
        LocalDateTime endTime = LocalDateTime.of(2026, 5, 20, 11, 0);
        TimePeriod timePeriod = new TimePeriod(startTime, endTime);
        Appointment appointment = new Appointment(patientId, timePeriod);

        appointment.cancel(Appointment.CancellationInitiator.PATIENT);

        assertThrows(AppointmentException.class, appointment::checkIn);
    }

    @Test
    void testCompleteAppointment(){
        PatientId patientId = new PatientId("patient-123");
        LocalDateTime startTime = LocalDateTime.of(2026, 5, 20, 10, 0);
        LocalDateTime endTime = LocalDateTime.of(2026, 5, 20, 11, 0);
        TimePeriod timePeriod = new TimePeriod(startTime, endTime);
        Appointment appointment = new Appointment(patientId, timePeriod);
        appointment.checkIn();

        appointment.complete();

        assertEquals(Appointment.status.COMPLETED, appointment.getAppointmentStatus());
    }

    @Test
    void testCompleteAppointmentOnWrongStatus(){
        PatientId patientId = new PatientId("patient-123");
        LocalDateTime startTime = LocalDateTime.of(2026, 5, 20, 10, 0);
        LocalDateTime endTime = LocalDateTime.of(2026, 5, 20, 11, 0);
        TimePeriod timePeriod = new TimePeriod(startTime, endTime);
        Appointment appointment = new Appointment(patientId, timePeriod);

        appointment.cancel(Appointment.CancellationInitiator.PATIENT);

        assertThrows(AppointmentException.class, appointment::complete);
    }
    @Test
    void testMarkOutAsNoShow(){
        PatientId patientId = new PatientId("patient-123");
        LocalDateTime startTime = LocalDateTime.of(2026, 5, 20, 10, 0);
        LocalDateTime endTime = LocalDateTime.of(2026, 5, 20, 11, 0);
        TimePeriod timePeriod = new TimePeriod(startTime, endTime);
        Appointment appointment = new Appointment(patientId, timePeriod);
        appointment.checkIn();

        appointment.markAsNoShow();

        assertEquals(Appointment.status.NO_SHOW, appointment.getAppointmentStatus());
    }
    @Test
    void testMarkOutAsNoShowOnWrongStatus(){
        PatientId patientId = new PatientId("patient-123");
        LocalDateTime startTime = LocalDateTime.of(2026, 5, 20, 10, 0);
        LocalDateTime endTime = LocalDateTime.of(2026, 5, 20, 11, 0);
        TimePeriod timePeriod = new TimePeriod(startTime, endTime);
        Appointment appointment = new Appointment(patientId, timePeriod);
        appointment.checkIn();

        appointment.complete();

        assertThrows(AppointmentException.class, appointment::markAsNoShow);
    }
    @Test
    void testAppointmentIsScheduled(){
        PatientId patientId = new PatientId("patient-123");
        LocalDateTime startTime = LocalDateTime.of(2026, 5, 20, 10, 0);
        LocalDateTime endTime = LocalDateTime.of(2026, 5, 20, 11, 0);
        TimePeriod timePeriod = new TimePeriod(startTime, endTime);

        Appointment appointment = new Appointment(patientId, timePeriod);

        assertTrue(appointment.isScheduled());
    }

    @Test
    void testAppointmentIsCanceledByTheSystem(){
        PatientId patientId = new PatientId("patient-123");
        LocalDateTime startTime = LocalDateTime.of(2026, 5, 20, 10, 0);
        LocalDateTime endTime = LocalDateTime.of(2026, 5, 20, 11, 0);
        TimePeriod timePeriod = new TimePeriod(startTime, endTime);

        Appointment appointment = new Appointment(patientId, timePeriod);
        appointment.cancel(Appointment.CancellationInitiator.SYSTEM_AUTOMATION);

        assertTrue(appointment.canceledBySystem());
    }
    @Test
    void testAppointmentIsNotCanceledByTheSystem(){
        PatientId patientId = new PatientId("patient-123");
        LocalDateTime startTime = LocalDateTime.of(2026, 5, 20, 10, 0);
        LocalDateTime endTime = LocalDateTime.of(2026, 5, 20, 11, 0);
        TimePeriod timePeriod = new TimePeriod(startTime, endTime);

        Appointment appointment = new Appointment(patientId, timePeriod);

        assertFalse(appointment.canceledBySystem());
    }
    @Test
    void testAppointmentIsRescheduled(){
        PatientId patientId = new PatientId("patient-123");
        LocalDateTime startTime = LocalDateTime.of(2026, 5, 20, 10, 0);
        LocalDateTime endTime = LocalDateTime.of(2026, 5, 20, 11, 0);
        TimePeriod timePeriod = new TimePeriod(startTime, endTime);
        AppointmentId appointmentId = new AppointmentId("appointment-123");

        Appointment appointment = new Appointment(patientId, timePeriod, appointmentId);

        assertEquals(appointmentId, appointment.getRescheduledFromId());
    }
    @Test
    void testPatientHasCheckedIn(){
        PatientId patientId = new PatientId("patient-123");
        LocalDateTime startTime = LocalDateTime.of(2026, 5, 20, 10, 0);
        LocalDateTime endTime = LocalDateTime.of(2026, 5, 20, 11, 0);
        TimePeriod timePeriod = new TimePeriod(startTime, endTime);
        Appointment appointment = new Appointment(patientId, timePeriod);

        appointment.checkIn();

        assertTrue(appointment.hasCheckedIn());
    }
    @Test
    void testPatientHasNotCheckedIn(){
        PatientId patientId = new PatientId("patient-123");
        LocalDateTime startTime = LocalDateTime.of(2026, 5, 20, 10, 0);
        LocalDateTime endTime = LocalDateTime.of(2026, 5, 20, 11, 0);
        TimePeriod timePeriod = new TimePeriod(startTime, endTime);

        Appointment appointment = new Appointment(patientId, timePeriod);

        assertFalse(appointment.hasCheckedIn());
    }

}
