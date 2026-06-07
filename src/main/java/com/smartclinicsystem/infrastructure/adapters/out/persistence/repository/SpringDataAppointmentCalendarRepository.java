package com.smartclinicsystem.infrastructure.adapters.out.persistence.repository;

import com.smartclinicsystem.infrastructure.adapters.out.persistence.JpaEntities.AppointmentCalendarEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SpringDataAppointmentCalendarRepository extends JpaRepository<AppointmentCalendarEntity, String> {


    @Query("""
        SELECT c FROM AppointmentCalendarEntity c
        LEFT JOIN FETCH c.unavailabilities
        LEFT JOIN FETCH c.effectiveSchedules
        WHERE c.doctorId = :doctorId
    """)
    Optional<AppointmentCalendarEntity> findByDoctorIdWithRules(@Param("doctorId") String doctorId);
}
