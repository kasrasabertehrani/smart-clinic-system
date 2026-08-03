package com.smartclinicsystem.infrastructure.adapters.out.persistence;

import com.smartclinicsystem.application.port.out.AppointmentCalendarRepositoryPort;
import com.smartclinicsystem.domain.AppointmentCalendar;
import com.smartclinicsystem.domain.vo.DoctorId;
import com.smartclinicsystem.infrastructure.adapters.out.persistence.exception.DatabaseOperationException;
import com.smartclinicsystem.infrastructure.adapters.out.persistence.exception.DuplicateResourceException;
import com.smartclinicsystem.infrastructure.adapters.out.persistence.JpaEntities.AppointmentCalendarEntity;
import com.smartclinicsystem.infrastructure.adapters.out.persistence.mapper.AppointmentCalendarMapper;
import com.smartclinicsystem.infrastructure.adapters.out.persistence.repository.SpringDataAppointmentCalendarRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class AppointmentCalendarRepositoryAdapter implements AppointmentCalendarRepositoryPort {

    private final SpringDataAppointmentCalendarRepository jpaRepository;
    private final AppointmentCalendarMapper mapper;

    public AppointmentCalendarRepositoryAdapter(
            SpringDataAppointmentCalendarRepository jpaRepository,
            AppointmentCalendarMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public AppointmentCalendar findByDoctorId(DoctorId doctorId) {
        try {
            Optional<AppointmentCalendarEntity> entityOptional =
                    jpaRepository.findByDoctorIdWithRules(doctorId.value());

            return entityOptional
                    .map(mapper::toCalendar)
                    .orElseGet(() -> new AppointmentCalendar(doctorId));
        } catch(DataAccessException e) {
            throw new DatabaseOperationException("Failed to retrieve calendar data from the database.", e);
        }
    }

    @Override
    public void save(AppointmentCalendar appointmentCalendar) {
         try {
             AppointmentCalendarEntity entity = mapper.toEntity(appointmentCalendar);
             jpaRepository.save(entity);
         } catch(DataIntegrityViolationException e) {
             throw new DuplicateResourceException(
                     "Failed to save calendar: A conflicting record already exists for this doctor.");
         } catch (DataAccessException e) {
             throw new DatabaseOperationException(
                     "An unexpected database error occurred while saving the calendar.", e);
         }
    }
}