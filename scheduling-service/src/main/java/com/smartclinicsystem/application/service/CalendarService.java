package com.smartclinicsystem.application.service;

import com.smartclinicsystem.application.command.AddUnavailabilityCommand;
import com.smartclinicsystem.application.command.ChangeScheduleCommand;
import com.smartclinicsystem.application.port.in.CalendarUseCase;
import com.smartclinicsystem.application.port.out.AppointmentCalendarRepositoryPort;
import com.smartclinicsystem.application.port.out.AppointmentRepositoryPort;
import com.smartclinicsystem.application.port.out.DomainEventPublisher;
import com.smartclinicsystem.domain.Appointment;
import com.smartclinicsystem.domain.AppointmentCalendar;
import com.smartclinicsystem.domain.service.AppointmentConflictDomainService;
import com.smartclinicsystem.domain.vo.DoctorId;
import com.smartclinicsystem.domain.vo.EffectiveSchedule;
import com.smartclinicsystem.domain.vo.Unavailability;
import com.smartclinicsystem.infrastructure.adapters.in.web.DTO.response.AddUnavailabilityResponse;
import com.smartclinicsystem.infrastructure.adapters.in.web.DTO.response.ChangeScheduleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.function.Function;

import java.util.List;
@Service
@Transactional
@RequiredArgsConstructor
public class CalendarService implements CalendarUseCase {
    private final AppointmentRepositoryPort appointmentRepository;
    private final AppointmentCalendarRepositoryPort appointmentCalendarRepository;
    private final AppointmentConflictDomainService appointmentConflictDomainService;
    private final DomainEventPublisher domainEventPublisher;


    @Override
    public AddUnavailabilityResponse addUnavailability(AddUnavailabilityCommand command) {
        AppointmentCalendar appointmentCalendar = appointmentCalendarRepository.findByDoctorId(command.doctorId());
        Unavailability newUnavailability = appointmentCalendar.addUnavailability(command.timePeriod());

        List<Appointment> canceledAppointments = cancelConflictsAndSave(
                command.doctorId(),
                appointmentCalendar,
                candidates -> appointmentConflictDomainService.findOverlapsWithUnavailability(candidates, newUnavailability)
        );
        return AddUnavailabilityResponse.from(newUnavailability, canceledAppointments);
    }
    @Override
    public ChangeScheduleResponse changeSchedule(ChangeScheduleCommand command) {
        AppointmentCalendar appointmentCalendar = appointmentCalendarRepository.findByDoctorId(command.doctorId());
        EffectiveSchedule changedSchedule = appointmentCalendar.changeSchedule(command.newSchedule(), command.validFrom());

        List<Appointment> canceledAppointments = cancelConflictsAndSave(
                command.doctorId(),
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
            appointment.getDomainEvents().forEach(domainEventPublisher::publish);
            appointment.clearDomainEvents();
        }
        appointmentRepository.saveAll(conflictingAppointments);
        appointmentCalendarRepository.save(calendar);
        return conflictingAppointments;
    }


}
