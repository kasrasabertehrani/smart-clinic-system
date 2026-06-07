package com.smartclinicsystem.domain;

import com.smartclinicsystem.domain.exception.AppointmentException;
import com.smartclinicsystem.domain.vo.AppointmentId;
import com.smartclinicsystem.domain.vo.DoctorId;
import com.smartclinicsystem.domain.vo.PatientId;
import com.smartclinicsystem.domain.vo.TimeSlot;
import lombok.Getter;


import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class Appointment {
    public enum status  {SCHEDULED, CHECKED_IN, COMPLETED, CANCELLED, NO_SHOW}
    public enum CancellationInitiator { PATIENT, CLINIC_RECEPTION, SYSTEM_AUTOMATION }

    private final AppointmentId id;
    private AppointmentId rescheduledFromId;
    private final PatientId patientId;
    private final DoctorId doctorId;
    private final TimeSlot appointmentTimeSlot;
    private status appointmentStatus;
    private CancellationInitiator cancelledBy;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Appointment(DoctorId doctorId,PatientId patientId, TimeSlot appointmentTimeSlot) {

        if(appointmentTimeSlot.date().isBefore(LocalDateTime.now().toLocalDate())) {
            throw new AppointmentException("Cannot schedule an appointment in the past.");
        }

        if (appointmentTimeSlot.duration().toMinutes() != 60) {
            throw new AppointmentException("Only 1-hour appointments are supported.");
        }

        this.id = new AppointmentId(UUID.randomUUID().toString());
        this.doctorId = doctorId;
        this.patientId = patientId;
        this.appointmentTimeSlot = appointmentTimeSlot;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = createdAt;
        this.appointmentStatus = status.SCHEDULED;
    }

    public Appointment(DoctorId doctorId,PatientId patientId, TimeSlot appointmentTimeSlot, AppointmentId rescheduledFromId) {
        this(doctorId ,patientId, appointmentTimeSlot);
        this.rescheduledFromId = rescheduledFromId;
    }


    public Appointment(AppointmentId id,
                       DoctorId doctorId,
                       PatientId patientId,
                       TimeSlot appointmentTimeSlot,
                       status appointmentStatus,
                       CancellationInitiator cancelledBy,
                       AppointmentId rescheduledFromId,
                       LocalDateTime createdAt,
                       LocalDateTime updatedAt) {


        this.id = id;
        this.doctorId = doctorId;
        this.patientId = patientId;
        this.appointmentTimeSlot = appointmentTimeSlot;
        this.appointmentStatus = appointmentStatus;
        this.cancelledBy = cancelledBy;
        this.rescheduledFromId = rescheduledFromId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public void cancel(CancellationInitiator initiator) {
        if (isScheduled() || appointmentStatus == status.CHECKED_IN) {
            this.appointmentStatus = status.CANCELLED;
            this.cancelledBy = initiator;
            this.updatedAt = LocalDateTime.now();
        } else {
            throw new AppointmentException("Only SCHEDULED or CHECKED_IN appointments can be cancelled.");
        }
    }
    public void complete() {
        if (appointmentStatus == status.CHECKED_IN) {
            appointmentStatus = status.COMPLETED;
            updatedAt = LocalDateTime.now();
        }
        else {
            throw new AppointmentException("Only CHECKED_IN appointments can be marked as COMPLETED.");
        }

    }

    public void checkIn() {
        if (this.appointmentStatus != status.SCHEDULED) {
            throw new AppointmentException("Can only check-in a SCHEDULED appointment.");
        }
        this.appointmentStatus = status.CHECKED_IN;
        this.updatedAt = LocalDateTime.now();
    }

    public void markAsNoShow() {
        if (this.appointmentStatus != status.SCHEDULED) {
            throw new AppointmentException("Only SCHEDULED appointments can be marked as No-Show.");
        }
        this.appointmentStatus = status.NO_SHOW;
        this.updatedAt = LocalDateTime.now();
    }

    public Appointment rescheduleActiveAppointment(TimeSlot newTimeSlot, CancellationInitiator initiator) {
        if (!isScheduled() && !hasCheckedIn()) {
            throw new AppointmentException("Only active appointments can be actively rescheduled.");
        }
        this.cancel(initiator);
        return new Appointment(this.doctorId, this.patientId, newTimeSlot, this.id);
    }

    public Appointment rescheduleSystemCancelledAppointment(TimeSlot newTimeSlot) {
        if (!canceledBySystem()) {
            throw new AppointmentException(
                    "This method is only for replacing appointments that the system previously cancelled.");
        }
        return new Appointment(this.doctorId, this.patientId, newTimeSlot, this.id);
    }

    public boolean isScheduled() {
        return this.appointmentStatus == status.SCHEDULED;
    }

    public boolean canceledBySystem() {
        return this.cancelledBy == CancellationInitiator.SYSTEM_AUTOMATION;
    }

    public boolean hasCheckedIn() {
        return this.appointmentStatus == status.CHECKED_IN;
    }

}
