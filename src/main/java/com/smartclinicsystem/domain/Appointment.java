package com.smartclinicsystem.domain;

import com.smartclinicsystem.domain.vo.AppointmentId;
import com.smartclinicsystem.domain.vo.PatientId;
import com.smartclinicsystem.domain.vo.TimePeriod;
import lombok.Getter;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class Appointment {
    public enum status  {SCHEDULED, CHECKED_IN, COMPLETED, CANCELLED, NO_SHOW}
    public enum CancellationInitiator { PATIENT, CLINIC_RECEPTION, SYSTEM_AUTOMATION }

    private final AppointmentId id;
    private AppointmentId rescheduledFromId;
    private final PatientId patientId;
    private final TimePeriod timePeriod;
    private status appointmentStatus;
    private CancellationInitiator cancelledBy;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Appointment(PatientId patientId, TimePeriod timePeriod) {

        if (timePeriod.startTime().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Appointment time cannot be in the past.");
        }

        long durationInMinutes = Duration.between(timePeriod.startTime(), timePeriod.endTime()).toMinutes();
        if (durationInMinutes != 60) {
            throw new IllegalArgumentException("Appointments must be strictly one hour long.");
        }

        this.id = new AppointmentId(UUID.randomUUID().toString());
        this.patientId = patientId;
        this.timePeriod = timePeriod;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = createdAt;
        this.appointmentStatus = status.SCHEDULED;
    }

    public Appointment(PatientId patientId, TimePeriod timePeriod, AppointmentId rescheduledFromId) {
        this(patientId, timePeriod);
        this.rescheduledFromId = rescheduledFromId;
    }

    public void cancel(CancellationInitiator initiator) {
        if (isScheduled() || appointmentStatus == status.CHECKED_IN) {
            this.appointmentStatus = status.CANCELLED;
            this.cancelledBy = initiator;
            this.updatedAt = LocalDateTime.now();
        } else {
            throw new IllegalStateException("Only SCHEDULED or CHECKED_IN appointments can be cancelled.");
        }
    }
    public void complete() {
        if (appointmentStatus == status.CHECKED_IN) {
            appointmentStatus = status.COMPLETED;
            updatedAt = LocalDateTime.now();
        }
        else {
            throw new IllegalStateException("Only CHECKED_IN appointments can be marked as COMPLETED.");
        }

    }

    public void checkIn() {
        if (this.appointmentStatus != status.SCHEDULED) {
            throw new IllegalStateException("Can only check-in a SCHEDULED appointment.");
        }
        this.appointmentStatus = status.CHECKED_IN;
        this.updatedAt = LocalDateTime.now();
    }

    public void markAsNoShow() {
        if (this.appointmentStatus != status.CHECKED_IN) {
            throw new IllegalStateException("Only CHECKED_IN appointments can be marked as No-Show.");
        }
        this.appointmentStatus = status.NO_SHOW;
        this.updatedAt = LocalDateTime.now();
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
