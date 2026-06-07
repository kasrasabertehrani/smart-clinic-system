package com.smartclinicsystem.infrastructure.adapters.out.persistence;



import com.smartclinicsystem.application.port.out.AppointmentRepositoryPort;
import com.smartclinicsystem.domain.Appointment;
import com.smartclinicsystem.domain.exception.AppointmentException;
import com.smartclinicsystem.domain.vo.AppointmentId;
import com.smartclinicsystem.domain.vo.DoctorId;
import com.smartclinicsystem.domain.vo.SharpTime;
import com.smartclinicsystem.domain.vo.TimeSlot;
import com.smartclinicsystem.infrastructure.adapters.out.persistence.exception.DatabaseOperationException;
import com.smartclinicsystem.infrastructure.adapters.out.persistence.exception.DuplicateResourceException;
import com.smartclinicsystem.infrastructure.adapters.out.persistence.JpaEntities.AppointmentEntity;
import com.smartclinicsystem.infrastructure.adapters.out.persistence.mapper.AppointmentMapper;
import com.smartclinicsystem.infrastructure.adapters.out.persistence.repository.SpringDataAppointmentRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class AppointmentRepositoryAdapter implements AppointmentRepositoryPort {

    private final SpringDataAppointmentRepository jpaRepository;
    private final AppointmentMapper mapper;

    public AppointmentRepositoryAdapter(SpringDataAppointmentRepository jpaRepository, AppointmentMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public List<Appointment> findByDoctorId(DoctorId doctorId) {
        try {
            List<AppointmentEntity> entities = jpaRepository.findByDoctorId(doctorId.value());

            return entities.stream()
                    .map(mapper::toAppointment)
                    .collect(Collectors.toList());
        } catch(DataAccessException e) {
            throw new DatabaseOperationException("Failed to retrieve appointments from the database.", e);
        }
    }

    @Override
    public void save(Appointment appointment) {
        try {
            AppointmentEntity entity = mapper.toEntity(appointment);
            jpaRepository.save(entity);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateResourceException("Failed to save appointment: A conflicting record already exists.");
        } catch (DataAccessException e) {
            throw new DatabaseOperationException("An unexpected database error occurred while saving the appointment.", e);
        }
    }

    @Override
    public List<TimeSlot> findTimeSlotByDoctorId(DoctorId doctorId) {
        try {
            List<AppointmentEntity> activeEntities = jpaRepository.findFutureActiveAppointments(doctorId.value());
            return activeEntities.stream()
                    .map(entity -> {
                        LocalDate appointmentDate = entity.getAppointmentDate();
                        SharpTime startTime = new SharpTime(entity.getStartTime());
                        Duration duration = Duration.between(entity.getStartTime(), entity.getEndTime());
                        return new TimeSlot(appointmentDate, startTime, duration);
                    })
                    .collect(Collectors.toList());
        } catch (DataAccessException e) {
            throw new DatabaseOperationException("Failed to retrieve active time slots from the database.", e);
        }
    }

    @Override
    public Appointment findAppointmentId(AppointmentId appointmentId) {
        try {
            AppointmentEntity entity = jpaRepository.findById(appointmentId.value())
                    .orElseThrow(() -> new AppointmentException("Appointment not found with ID: " + appointmentId.value()));
            return mapper.toAppointment(entity);
        } catch (DataAccessException e) {
            throw new DatabaseOperationException("An error occurred while fetching the appointment by ID.", e);
        }
    }

    @Override
    public void saveAll(List<Appointment> appointments) {
        try {
            List<AppointmentEntity> entities = appointments.stream()
                    .map(mapper::toEntity)
                    .collect(Collectors.toList());
            jpaRepository.saveAll(entities);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateResourceException("Failed to save batch appointments: A conflicting record already exists.");
        } catch (DataAccessException e) {
            throw new DatabaseOperationException("An unexpected database error occurred while saving the batch.", e);
        }
    }
}