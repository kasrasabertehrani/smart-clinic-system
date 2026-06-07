package com.smartclinicsystem.infrastructure.adapters.out.persistence.mapper;

import com.smartclinicsystem.domain.Appointment;
import com.smartclinicsystem.domain.vo.*;
import com.smartclinicsystem.infrastructure.adapters.out.persistence.JpaEntities.AppointmentEntity;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDate;


@Component
public class AppointmentMapper {

    public AppointmentEntity toEntity(Appointment appointment) {
        if (appointment == null) return null;

        return new AppointmentEntity(
                appointment.getId().value(),
                appointment.getDoctorId().value(),
                appointment.getPatientId().value(),
                appointment.getAppointmentTimeSlot().date(),
                appointment.getAppointmentTimeSlot().start().time(),
                appointment.getAppointmentTimeSlot().getEnd().time(),
                appointment.getAppointmentStatus(),
                appointment.getCancelledBy(),
                appointment.getCreatedAt(),
                appointment.getUpdatedAt(),
                appointment.getRescheduledFromId().value()
        );
    }


    public Appointment toAppointment(AppointmentEntity entity) {
        if (entity == null) return null;


        LocalDate appointmentDate = entity.getAppointmentDate();
        SharpTime startTime = new SharpTime(entity.getStartTime());
        Duration duration = Duration.between(entity.getStartTime(), entity.getEndTime());
        TimeSlot timeSlot = new TimeSlot(appointmentDate, startTime, duration);


        return new Appointment(
                new AppointmentId(entity.getId()),
                new DoctorId(entity.getDoctorId()),
                new PatientId(entity.getPatientId()),
                timeSlot,
                entity.getStatus(),
                entity.getCanceledBy(),
                new AppointmentId(entity.getRescheduledFromId()),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}