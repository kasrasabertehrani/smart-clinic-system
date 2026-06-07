package com.smartclinicsystem.application.service;

import com.smartclinicsystem.application.exception.ConcurrentOperationException;
import com.smartclinicsystem.application.port.in.SchedulingUseCase;
import com.smartclinicsystem.application.port.out.AppointmentCalendarRepositoryPort;
import com.smartclinicsystem.application.port.out.AppointmentRepositoryPort;
import com.smartclinicsystem.domain.Appointment;
import com.smartclinicsystem.domain.AppointmentCalendar;
import com.smartclinicsystem.domain.vo.*;
import com.smartclinicsystem.infrastructure.adapters.in.DTO.request.AddAppointmentCommand;
import com.smartclinicsystem.infrastructure.adapters.in.DTO.request.CancelAppointmentCommand;
import com.smartclinicsystem.infrastructure.adapters.in.DTO.request.RescheduleAppointmentCommand;
import com.smartclinicsystem.infrastructure.adapters.in.DTO.request.RescheduleSystemCanceledCommand;
import com.smartclinicsystem.infrastructure.adapters.in.DTO.response.AppointmentResponse;
import com.smartclinicsystem.infrastructure.adapters.in.DTO.response.CancelAppointmentResponse;
import com.smartclinicsystem.infrastructure.adapters.in.DTO.response.RescheduleAppointmentResponse;
import jakarta.transaction.Transactional;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;


import java.util.List;


@Service
@Transactional
public class SchedulingService implements SchedulingUseCase {
    private final AppointmentRepositoryPort appointmentRepository;
    private final AppointmentCalendarRepositoryPort appointmentCalendarRepository;

    public SchedulingService(AppointmentRepositoryPort appointmentRepository,
                             AppointmentCalendarRepositoryPort appointmentCalendarRepository) {
        this.appointmentRepository = appointmentRepository;
        this.appointmentCalendarRepository = appointmentCalendarRepository;
    }

    @Override
    public AppointmentResponse addAppointment(AddAppointmentCommand command) {
        DoctorId doctorId = command.getDoctorId();
        PatientId patientId = command.getPatientId();
        TimeSlot timeSlot = command.createTimeSlot();

        validateTimeSlotAvailability(doctorId, timeSlot);
        Appointment newAppointment = new Appointment(doctorId, patientId, timeSlot);
        saveWithConcurrencyCheck(newAppointment);
        return AppointmentResponse.from(newAppointment);
    }
    @Override
    public CancelAppointmentResponse cancelAppointment(CancelAppointmentCommand command, String appointment_Id) {
        AppointmentId appointmentId = new AppointmentId(appointment_Id);
        Appointment appointmentToCancel = appointmentRepository.findAppointmentId(appointmentId);
        appointmentToCancel.cancel(command.getCancelInitiator());
        saveWithConcurrencyCheck(appointmentToCancel);
        return CancelAppointmentResponse.from(appointmentToCancel);
    }

    @Override
    public AppointmentResponse checkInPatient(String appointment_id) {
        AppointmentId appointmentId = new AppointmentId(appointment_id);
        Appointment appointment = appointmentRepository.findAppointmentId(appointmentId);
        appointment.checkIn();
        saveWithConcurrencyCheck(appointment);
        return AppointmentResponse.from(appointment);
    }
    @Override
    public AppointmentResponse completeAppointment(String appointment_id) {
        AppointmentId appointmentId = new AppointmentId(appointment_id);
        Appointment appointment = appointmentRepository.findAppointmentId(appointmentId);
        appointment.complete();
        saveWithConcurrencyCheck(appointment);
        return AppointmentResponse.from(appointment);
    }
    @Override
    public AppointmentResponse markAsNoShow(String appointment_id) {
        AppointmentId appointmentId = new AppointmentId(appointment_id);
        Appointment appointment = appointmentRepository.findAppointmentId(appointmentId);
        appointment.markAsNoShow();
        saveWithConcurrencyCheck(appointment);
        return AppointmentResponse.from(appointment);
    }

    @Override
    public RescheduleAppointmentResponse rescheduleActiveAppointment(RescheduleAppointmentCommand command, String appointment_id,
                                            String doctor_id) {
        AppointmentId appointmentId = new AppointmentId(appointment_id);
        Appointment oldAppointment = appointmentRepository.findAppointmentId(appointmentId);
        validateTimeSlotAvailability(oldAppointment.getDoctorId(), command.createTimeSlot());

        Appointment newAppointment = oldAppointment.rescheduleActiveAppointment(
                command.createTimeSlot(),
                command.getCancelInitiator()
        );
        saveWithConcurrencyCheck(oldAppointment);
        saveWithConcurrencyCheck(newAppointment);
        return RescheduleAppointmentResponse.from(newAppointment);
    }
    @Override
    public RescheduleAppointmentResponse rescheduleSystemCanceledAppointment(RescheduleSystemCanceledCommand command, String appointment_id,
                                                                             String doctor_id) {
        AppointmentId appointmentId = new AppointmentId(appointment_id);
        Appointment oldAppointment = appointmentRepository.findAppointmentId(appointmentId);
        validateTimeSlotAvailability(oldAppointment.getDoctorId(), command.createTimeSlot());

        Appointment newAppointment = oldAppointment.rescheduleSystemCancelledAppointment(command.createTimeSlot());

        saveWithConcurrencyCheck(oldAppointment);
        saveWithConcurrencyCheck(newAppointment);
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
