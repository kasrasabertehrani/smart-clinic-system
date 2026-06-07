package com.smartclinicsystem.infrastructure.adapters.out.persistence.JpaEntities;

import com.smartclinicsystem.domain.Appointment;
import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.Id;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Entity
@Table(name = "appointments")
public class AppointmentEntity {

    @Id
    private String id;

    @Column(name = "doctor_id", nullable = false)
    private String doctorId;

    @Column(name = "patient_id", nullable = false)
    private String patientId;

    @Column(name = "appointment_date", nullable = false)
    private LocalDate appointmentDate;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Appointment.status status;

    @Enumerated(EnumType.STRING)
    @Column(name = "canceled_by")
    private Appointment.CancellationInitiator canceledBy;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "rescheduled_from_id")
    private String rescheduledFromId;


    protected AppointmentEntity() {

    }

    public AppointmentEntity(String id, String doctorId, String patientId,
                             LocalDate appointmentDate, LocalTime startTime, LocalTime endTime,
                             Appointment.status status, Appointment.CancellationInitiator canceledBy,
                             LocalDateTime createdAt, LocalDateTime updatedAt, String rescheduledFromId) {
        this.id = id;
        this.doctorId = doctorId;
        this.patientId = patientId;
        this.appointmentDate = appointmentDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
        this.canceledBy = canceledBy;
        this.rescheduledFromId = rescheduledFromId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

}