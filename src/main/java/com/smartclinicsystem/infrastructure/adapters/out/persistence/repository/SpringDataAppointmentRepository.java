package com.smartclinicsystem.infrastructure.adapters.out.persistence.repository;

import com.smartclinicsystem.infrastructure.adapters.out.persistence.JpaEntities.AppointmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SpringDataAppointmentRepository extends JpaRepository<AppointmentEntity, String> {


    List<AppointmentEntity> findByDoctorId(String doctorId);


    @Query("""
        SELECT a FROM AppointmentEntity a 
        WHERE a.doctorId = :doctorId 
        AND a.appointmentDate >= CURRENT_DATE 
        AND a.status = 'SCHEDULED'
    """)
    List<AppointmentEntity> findFutureActiveAppointments(@Param("doctorId") String doctorId);
}