package com.smartclinicsystem.infrastructure.adapters.out.persistence.dao;

import com.smartclinicsystem.domain.Appointment;
import com.smartclinicsystem.infrastructure.adapters.in.web.DTO.response.AppointmentResponse;
import com.smartclinicsystem.infrastructure.adapters.out.persistence.JpaEntities.AppointmentEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

@org.springframework.stereotype.Repository
public interface AppointmentQueryDao extends Repository<AppointmentEntity, String> {

    @Query("SELECT new com.smartclinicsystem.infrastructure.adapters.in.web.DTO.response.AppointmentResponse(" +
            "i.id, i.doctorId, i.patientId, i.appointmentDate, i.startTime, i.endTime, CAST(i.status AS string)) " +
            "FROM AppointmentEntity i WHERE i.status = :status ORDER BY i.createdAt DESC LIMIT 1")
    Optional<AppointmentResponse> getAppointmentStatus(@Param("status") Appointment.status status);

    @Query("SELECT new com.smartclinicsystem.infrastructure.adapters.in.web.DTO.response.AppointmentResponse(" +
            "i.id, i.doctorId, i.patientId, i.appointmentDate, i.startTime, i.endTime, CAST(i.status AS string)) " +
            "FROM AppointmentEntity i WHERE i.id = :id ORDER BY i.createdAt DESC LIMIT 1")
    Optional<AppointmentResponse> getAppointmentStatusById(@Param("id") String appointmentId);

}

