package com.smartclinicsystem.application.service;

import com.smartclinicsystem.application.command.AddAppointmentCommand;
import com.smartclinicsystem.application.command.CancelAppointmentCommand;
import com.smartclinicsystem.application.command.RescheduleAppointmentCommand;
import com.smartclinicsystem.application.command.RescheduleSystemCanceledCommand;
import com.smartclinicsystem.application.exception.ConcurrentOperationException;
import com.smartclinicsystem.application.port.in.SchedulingUseCase;
import com.smartclinicsystem.application.port.out.AppointmentCalendarRepositoryPort;
import com.smartclinicsystem.application.port.out.AppointmentRepositoryPort;
import com.smartclinicsystem.application.port.out.DomainEventPublisher;
import com.smartclinicsystem.domain.Appointment;
import com.smartclinicsystem.domain.AppointmentCalendar;
import com.smartclinicsystem.domain.vo.*;
import com.smartclinicsystem.infrastructure.adapters.in.web.DTO.response.AppointmentResponse;
import com.smartclinicsystem.infrastructure.adapters.in.web.DTO.response.CancelAppointmentResponse;
import com.smartclinicsystem.infrastructure.adapters.in.web.DTO.response.RescheduleAppointmentResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;


import java.util.List;


@Service
@Transactional
@RequiredArgsConstructor
public class SchedulingService implements SchedulingUseCase {
    private final AppointmentRepositoryPort appointmentRepository;
    private final AppointmentCalendarRepositoryPort appointmentCalendarRepository;
    private final DomainEventPublisher eventPublisher;



    @Override
    public AppointmentResponse addAppointment(AddAppointmentCommand command) {

        validateTimeSlotAvailability(command.doctorId(), command.timeSlot());
        Appointment newAppointment = new Appointment(command.doctorId(), command.patientId(), command.timeSlot());
        saveWithConcurrencyCheck(newAppointment);
        newAppointment.getDomainEvents().forEach(eventPublisher::publish);
        newAppointment.clearDomainEvents();
        return AppointmentResponse.from(newAppointment);
    }
    @Override
    public CancelAppointmentResponse cancelAppointment(CancelAppointmentCommand command) {
        Appointment appointmentToCancel = appointmentRepository.findAppointmentId(command.appointmentId());
        appointmentToCancel.cancel(command.cancelInitiator());
        saveWithConcurrencyCheck(appointmentToCancel);
        appointmentToCancel.getDomainEvents().forEach(eventPublisher::publish);
        appointmentToCancel.clearDomainEvents();
        return CancelAppointmentResponse.from(appointmentToCancel);
    }

    @Override
    public AppointmentResponse checkInPatient(AppointmentId appointmentId) {
        Appointment appointment = appointmentRepository.findAppointmentId(appointmentId);
        appointment.checkIn();
        saveWithConcurrencyCheck(appointment);
        return AppointmentResponse.from(appointment);
    }
    @Override
    public AppointmentResponse completeAppointment(AppointmentId appointmentId) {
        Appointment appointment = appointmentRepository.findAppointmentId(appointmentId);
        appointment.complete();
        saveWithConcurrencyCheck(appointment);
        return AppointmentResponse.from(appointment);
    }
    @Override
    public AppointmentResponse markAsNoShow(AppointmentId appointmentId) {
        Appointment appointment = appointmentRepository.findAppointmentId(appointmentId);
        appointment.markAsNoShow();
        saveWithConcurrencyCheck(appointment);
        return AppointmentResponse.from(appointment);
    }

    @Override
    public RescheduleAppointmentResponse rescheduleActiveAppointment(RescheduleAppointmentCommand command) {
        Appointment oldAppointment = appointmentRepository.findAppointmentId(command.appointmentId());
        validateTimeSlotAvailability(oldAppointment.getDoctorId(), command.newTimeSlot());

        Appointment newAppointment = oldAppointment.rescheduleActiveAppointment(
                command.newTimeSlot(),
                command.cancelInitiator()
        );
        saveWithConcurrencyCheck(oldAppointment);
        saveWithConcurrencyCheck(newAppointment);

        oldAppointment.getDomainEvents().forEach(eventPublisher::publish);
        oldAppointment.clearDomainEvents();

        newAppointment.getDomainEvents().forEach(eventPublisher::publish);
        newAppointment.clearDomainEvents();

        return RescheduleAppointmentResponse.from(newAppointment);
    }
    @Override
    public RescheduleAppointmentResponse rescheduleSystemCanceledAppointment(RescheduleSystemCanceledCommand command) {
        Appointment oldAppointment = appointmentRepository.findAppointmentId(command.appointmentId());
        validateTimeSlotAvailability(oldAppointment.getDoctorId(), command.newTimeSlot());

        Appointment newAppointment = oldAppointment.rescheduleSystemCancelledAppointment(command.newTimeSlot());

        saveWithConcurrencyCheck(oldAppointment);
        saveWithConcurrencyCheck(newAppointment);
        oldAppointment.getDomainEvents().forEach(eventPublisher::publish);
        oldAppointment.clearDomainEvents();
        return RescheduleAppointmentResponse.from(newAppointment);
    }

    private void validateTimeSlotAvailability(DoctorId doctorId, TimeSlot requestedTimeSlot) {
        List<TimeSlot> bookedSlots = appointmentRepository.findTimeSlotByDoctorId(doctorId);
        AppointmentCalendar appointmentCalendar = appointmentCalendarRepository.findByDoctorId(doctorId);

        appointmentCalendar.validateBookingRules(requestedTimeSlot, bookedSlots);
    }

    private void saveWithConcurrencyCheck(Appointment appointment) {
        try {
            appointmentRepository.save(appointment);
        } catch (ObjectOptimisticLockingFailureException e) {
            throw new ConcurrentOperationException(
                    "This appointment was just modified by another user. Please refresh and try again.");
        }
    }

}
