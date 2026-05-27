package com.smartclinicsystem.domain;

import com.smartclinicsystem.domain.exception.*;
import com.smartclinicsystem.domain.vo.*;
import lombok.Getter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Getter
public class AppointmentCalendar {
    private final List<EffectiveSchedule> effectiveSchedules;
    private final List<Appointment> appointments;
    private final List<Unavailability> unavailabilities;
    private final DoctorId doctorId;

    public AppointmentCalendar(
            DoctorId doctorId,
            List<EffectiveSchedule> effectiveSchedules,
            List<Unavailability> unavailabilities,
            List<Appointment> appointments) {

        this.doctorId = doctorId;
        this.effectiveSchedules = new ArrayList<>(effectiveSchedules);
        this.unavailabilities = new ArrayList<>(unavailabilities);
        this.appointments = new ArrayList<>(appointments);
    }

    public void addAppointment(PatientId patientId, TimeSlot appointmentTime) {
            validateBookingRules(appointmentTime);
            Appointment proposedAppointment = new Appointment(patientId, appointmentTime);
            appointments.add(proposedAppointment);
    }

    public void cancelAppointment(AppointmentId appointmentId, Appointment.CancellationInitiator initiator) {
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

    public void rescheduleActiveAppointment(
            AppointmentId oldAppointmentId,
            TimeSlot newTimeSlot,
            Appointment.CancellationInitiator initiator) {

        Appointment oldAppointment = findAppointmentOrThrow(oldAppointmentId);

        if (!oldAppointment.isScheduled() && !oldAppointment.hasCheckedIn()) {
            throw new BookingException("Only active appointments can be actively rescheduled.");
        }

        validateBookingRules(newTimeSlot);
        oldAppointment.cancel(initiator);


        Appointment rescheduledAppointment = new Appointment(
                oldAppointment.getPatientId(),
                newTimeSlot,
                oldAppointmentId
        );
        appointments.add(rescheduledAppointment);
    }

    public void rescheduleSystemCancelledAppointment(
            AppointmentId oldAppointmentId,
            TimeSlot newTimeSlot) {

        Appointment oldAppointment = findAppointmentOrThrow(oldAppointmentId);


        if (!oldAppointment.canceledBySystem()) {
            throw new BookingException(
                    "This method is only for replacing appointments that the system previously cancelled.");
        }

        validateBookingRules(newTimeSlot);


        Appointment rescheduledAppointment = new Appointment(
                oldAppointment.getPatientId(),
                newTimeSlot,
                oldAppointmentId
        );
        appointments.add(rescheduledAppointment);
    }


    private void updateAppointmentsOnNewUnavailability(Unavailability newUnavailability) {
        this.appointments.stream()
                .filter(Appointment::isScheduled)
                .filter(app -> newUnavailability.isUnavailableIn(app.getAppointmentTimeSlot()))
                .forEach(app -> app.cancel(Appointment.CancellationInitiator.CLINIC_RECEPTION));
    }

    private void updateAppointmentsOnScheduleChange(EffectiveSchedule newlyAddedSchedule) {
        this.appointments.stream()
                .filter(Appointment::isScheduled)
                .filter(app -> newlyAddedSchedule.appliesTo(app.getAppointmentTimeSlot().date()))
                .filter(app -> !newlyAddedSchedule.schedule().isWorkingDuring(app.getAppointmentTimeSlot()))
                .forEach(app -> app.cancel(Appointment.CancellationInitiator.CLINIC_RECEPTION));
    }

    private boolean isDoctorAvailable(TimeSlot requestedTime) {
        return this.unavailabilities.stream()
                .noneMatch(unavail -> unavail.isUnavailableIn(requestedTime));
    }

    private boolean isNotAlreadyBooked(TimeSlot requestedTimeSlot) {
        return this.appointments.stream()
                .filter(Appointment::isScheduled)
                .noneMatch(app -> app.getAppointmentTimeSlot().overlapsWith(requestedTimeSlot));
    }

    private boolean isDoctorWorking(TimeSlot requestedPeriod) {
        LocalDate requestedDate = requestedPeriod.date();

        EffectiveSchedule activeSchedule = this.effectiveSchedules.stream()
                .filter(schedule -> schedule.appliesTo(requestedDate))
                .max(Comparator.comparing(EffectiveSchedule::validFrom))
                .orElseThrow(() -> new InvalidEffectiveScheduleException(
                        "Cannot process request: No valid working schedule found for the date " + requestedDate
                ));

        return activeSchedule.schedule().isWorkingDuring(requestedPeriod);
    }

    public void addUnavailability(TimePeriod leavePeriod) {
        Unavailability newUnavailability = new Unavailability(leavePeriod);
        this.unavailabilities.add(newUnavailability);
        updateAppointmentsOnNewUnavailability(newUnavailability);
    }

    public void changeSchedule(WeeklySchedule newSchedule, LocalDate effectiveDate) {
        boolean dateAlreadyExists = this.effectiveSchedules.stream()
                .anyMatch(existing -> existing.validFrom().equals(effectiveDate));

        if (dateAlreadyExists) {
            throw new InvalidEffectiveScheduleException(
                    "A schedule already exists starting on " + effectiveDate +
                            ". You must edit the existing schedule or choose a different start date."
            );
        }

        EffectiveSchedule newEffectiveSchedule = new EffectiveSchedule(effectiveDate, newSchedule);
        this.effectiveSchedules.add(newEffectiveSchedule);
        updateAppointmentsOnScheduleChange(newEffectiveSchedule);
    }

    public Appointment findAppointmentOrThrow(AppointmentId appointmentId) {
        return this.appointments.stream()
                .filter(app -> app.getId().equals(appointmentId))
                .findFirst()
                .orElseThrow(() -> new AppointmentNotFoundException(
                "Appointment with ID " + appointmentId.value() + " was not found in this calendar."
        ));
    }

    public void validateBookingRules(TimeSlot requestedTimeSlot) {
        if (!isDoctorWorking(requestedTimeSlot)) {
            throw new BookingException("Cannot book: The requested time falls outside working hours.");
        }
        if (!isDoctorAvailable(requestedTimeSlot)) {
            throw new BookingException("Cannot book: The doctor is on leave during this time.");
        }
        if (!isNotAlreadyBooked(requestedTimeSlot)) {
            throw new BookingException("Cannot book: This time slot is already taken.");
        }
    }

}
