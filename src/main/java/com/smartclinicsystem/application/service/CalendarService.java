package com.smartclinicsystem.application.service;

import com.smartclinicsystem.application.port.in.CalendarUseCase;
import com.smartclinicsystem.application.port.out.AppointmentCalendarRepositoryPort;
import com.smartclinicsystem.application.port.out.AppointmentRepositoryPort;
import com.smartclinicsystem.domain.Appointment;
import com.smartclinicsystem.domain.AppointmentCalendar;
import com.smartclinicsystem.domain.service.AppointmentConflictDomainService;
import com.smartclinicsystem.domain.vo.DoctorId;
import com.smartclinicsystem.domain.vo.EffectiveSchedule;
import com.smartclinicsystem.domain.vo.Unavailability;
import com.smartclinicsystem.infrastructure.adapters.in.DTO.request.AddUnavailabilityCommand;
import com.smartclinicsystem.infrastructure.adapters.in.DTO.request.ChangeScheduleCommand;
import com.smartclinicsystem.infrastructure.adapters.in.DTO.response.AddUnavailabilityResponse;
import com.smartclinicsystem.infrastructure.adapters.in.DTO.response.ChangeScheduleResponse;


import java.util.function.Function;

import java.util.List;

public class CalendarService implements CalendarUseCase {
    private final AppointmentRepositoryPort appointmentRepository;
    private final AppointmentCalendarRepositoryPort appointmentCalendarRepository;
    private final AppointmentConflictDomainService appointmentConflictDomainService;

    public CalendarService(AppointmentRepositoryPort appointmentRepository,
                           AppointmentCalendarRepositoryPort appointmentCalendarRepository,
                           AppointmentConflictDomainService appointmentConflictDomainService) {
        this.appointmentRepository = appointmentRepository;
        this.appointmentCalendarRepository = appointmentCalendarRepository;
        this.appointmentConflictDomainService = appointmentConflictDomainService;
    }
    @Override
    public AddUnavailabilityResponse addUnavailability(AddUnavailabilityCommand command, String doctor_id) {
        DoctorId doctorId = new DoctorId(doctor_id);
        AppointmentCalendar appointmentCalendar = appointmentCalendarRepository.findByDoctorId(doctorId);
        Unavailability newUnavailability = appointmentCalendar.addUnavailability(command.createTimePeriod());

        List<Appointment> canceledAppointments = cancelConflictsAndSave(
                doctorId,
                appointmentCalendar,
                candidates -> appointmentConflictDomainService.findOverlapsWithUnavailability(candidates, newUnavailability)
        );
        return AddUnavailabilityResponse.from(newUnavailability, canceledAppointments);
    }
    @Override
    public ChangeScheduleResponse changeSchedule(ChangeScheduleCommand command, String doctor_id) {
        DoctorId doctorId = new DoctorId(doctor_id);
        AppointmentCalendar appointmentCalendar = appointmentCalendarRepository.findByDoctorId(doctorId);
        EffectiveSchedule changedSchedule = appointmentCalendar.changeSchedule(command.toWeeklySchedule(), command.getValidFrom());

        List<Appointment> canceledAppointments = cancelConflictsAndSave(
                doctorId,
                appointmentCalendar,
                candidates -> appointmentConflictDomainService.findConflictsWithNewSchedule(candidates, changedSchedule)
        );
        return ChangeScheduleResponse.from(changedSchedule, canceledAppointments);
    }


    private List<Appointment> cancelConflictsAndSave(
            DoctorId doctorId,
            AppointmentCalendar calendar,
            Function<List<Appointment>, List<Appointment>> conflictFinder) {

        List<Appointment> candidateAppointments = appointmentRepository.findByDoctorId(doctorId);

        List<Appointment> conflictingAppointments = conflictFinder.apply(candidateAppointments);

        for (Appointment appointment : conflictingAppointments) {
            appointment.cancel(Appointment.CancellationInitiator.SYSTEM_AUTOMATION);
        }
        appointmentRepository.saveAll(conflictingAppointments);
        appointmentCalendarRepository.save(calendar);
        return conflictingAppointments;
    }


}
