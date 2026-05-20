package com.smartclinicsystem.domain;

import com.smartclinicsystem.domain.exception.*;
import com.smartclinicsystem.domain.vo.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class AppointmentCalendar {
    private final List<EffectiveSchedule> effectiveSchedules;
    private final List<Appointment> appointments;
    private final List<Unavailability> unavailabilities;

    public AppointmentCalendar(DoctorId doctorId, List<EffectiveSchedule> effectiveSchedules) {
        this.effectiveSchedules = effectiveSchedules;
        this.appointments = new ArrayList<>();
        this.unavailabilities = new ArrayList<>();
    }

    private void addAppointment(PatientId patientId, TimePeriod appointmentTime) {
            validateBookingRules(appointmentTime);
            Appointment proposedAppointment = new Appointment(patientId, appointmentTime);
            appointments.add(proposedAppointment);
    }

    private void cancelAppointment(AppointmentId appointmentId, Appointment.CancellationInitiator initiator) {
        Appointment appointmentToCancel = findAppointmentOrThrow(appointmentId);
        appointmentToCancel.cancel(initiator);
    }

    public void checkInPatient(AppointmentId appointmentId) {
        Appointment appointment = findAppointmentOrThrow(appointmentId);
        appointment.checkIn();
    }

    public void completeAppointment(AppointmentId appointmentId) {
        Appointment appointment = findAppointmentOrThrow(appointmentId);
        appointment.complete();
    }

    public void recordNoShow(AppointmentId appointmentId) {
        Appointment appointment = findAppointmentOrThrow(appointmentId);
        appointment.markAsNoShow();
    }

    public void rescheduleAppointment(AppointmentId oldAppointmentId, TimePeriod newTimePeriod,
                                      Appointment.CancellationInitiator initiator) {
        Appointment oldAppointment = findAppointmentOrThrow(oldAppointmentId);

        if (!oldAppointment.isScheduled() && !oldAppointment.hasCheckedIn() && !oldAppointment.canceledBySystem()) {
            throw new BookingException(
                    "Appointment can only be rescheduled if it is SCHEDULED, CHECKED_IN, or CANCELLED_BY_SYSTEM.");
        }
        validateBookingRules(newTimePeriod);

        if (oldAppointment.isScheduled() || oldAppointment.hasCheckedIn()) {
            oldAppointment.cancel(initiator);
        }
        Appointment rescheduledAppointment = new Appointment(oldAppointment.getPatientId(), newTimePeriod,
                oldAppointmentId);
        appointments.add(rescheduledAppointment);
    }


    private void updateAppointmentsOnNewUnavailability(TimePeriod newLeavePeriod) {
        this.appointments.stream()
                .filter(Appointment::isScheduled)
                .filter(app -> app.getTimePeriod().overlapsWith(newLeavePeriod))
                .forEach(app -> app.cancel(Appointment.CancellationInitiator.CLINIC_RECEPTION));
    }

    private void updateAppointmentsOnScheduleChange(EffectiveSchedule newlyAddedSchedule) {
        this.appointments.stream()
                .filter(Appointment::isScheduled)
                .filter(app -> newlyAddedSchedule.appliesTo(app.getTimePeriod().startTime().toLocalDate()))
                .filter(app -> !newlyAddedSchedule.schedule().isWorkingDuring(app.getTimePeriod()))
                .forEach(app -> app.cancel(Appointment.CancellationInitiator.CLINIC_RECEPTION));
    }

    private boolean isDoctorAvailable(TimePeriod requestedTime) {
        return this.unavailabilities.stream()
                .noneMatch(unavail -> unavail.isUnavailableIn(requestedTime));
    }

    private boolean isNotAlreadyBooked(TimePeriod requestedTime) {
        return this.appointments.stream()
                .filter(Appointment::isScheduled)
                .noneMatch(app -> app.getTimePeriod().overlapsWith(requestedTime));
    }

    private boolean isDoctorWorking(TimePeriod requestedPeriod) {
        LocalDate requestedDate = requestedPeriod.startTime().toLocalDate();

        EffectiveSchedule activeSchedule = this.effectiveSchedules.stream()
                .filter(schedule -> schedule.appliesTo(requestedDate))
                .max(Comparator.comparing(EffectiveSchedule::validFrom))
                .orElseThrow(() -> new InvalidEffectiveScheduleException(
                        "Cannot process request: No valid working schedule found for the date " + requestedDate
                ));

        return activeSchedule.schedule().isWorkingDuring(requestedPeriod);
    }

    public void addUnavailability(TimePeriod leavePeriod) {
        this.unavailabilities.add(new Unavailability(leavePeriod));
        updateAppointmentsOnNewUnavailability(leavePeriod);
    }

    public void changeSchedule(WeeklySchedule newSchedule, LocalDate effectiveDate) {
        EffectiveSchedule newEffectiveSchedule = new EffectiveSchedule(effectiveDate, newSchedule);
        this.effectiveSchedules.add(newEffectiveSchedule);
        updateAppointmentsOnScheduleChange(newEffectiveSchedule);
    }

    private Appointment findAppointmentOrThrow(AppointmentId appointmentId) {
        return this.appointments.stream()
                .filter(app -> app.getId().equals(appointmentId))
                .findFirst()
                .orElseThrow(() -> new AppointmentNotFoundException(
                "Appointment with ID " + appointmentId.value() + " was not found in this calendar."
        ));
    }

    private void validateBookingRules(TimePeriod requestedTime) {
        if (!isDoctorWorking(requestedTime)) {
            throw new BookingException("Cannot book: The requested time falls outside working hours.");
        }
        if (!isDoctorAvailable(requestedTime)) {
            throw new BookingException("Cannot book: The doctor is on leave during this time.");
        }
        if (!isNotAlreadyBooked(requestedTime)) {
            throw new BookingException("Cannot book: This time slot is already taken.");
        }
    }

}
