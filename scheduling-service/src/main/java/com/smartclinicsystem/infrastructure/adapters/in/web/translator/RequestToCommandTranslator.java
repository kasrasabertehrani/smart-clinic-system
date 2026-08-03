package com.smartclinicsystem.infrastructure.adapters.in.web.translator;

import com.smartclinicsystem.application.command.*;
import com.smartclinicsystem.domain.vo.*;
import com.smartclinicsystem.infrastructure.adapters.in.web.DTO.request.*;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.Duration;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class RequestToCommandTranslator {

    public AddAppointmentCommand translateToAddAppointment(AddAppointmentRequest request) {
        return new AddAppointmentCommand(
                new DoctorId(request.getDoctorId()),
                new PatientId(request.getPatientId()),
                new TimeSlot(request.getAppointmentDate(), new SharpTime(request.getStartTime()), Duration.ofMinutes(60))
        );
    }

    public CancelAppointmentCommand translateToCancelAppointment(CancelAppointmentRequest request, String appointmentId) {
        return new CancelAppointmentCommand(
                new AppointmentId(appointmentId),
                request.getCancelInitiator()
        );
    }

    public RescheduleAppointmentCommand translateToRescheduleAppointment(RescheduleAppointmentRequest request, String appointmentId) {
        return new RescheduleAppointmentCommand(
                new AppointmentId(appointmentId),
                new TimeSlot(request.getAppointmentDate(), new SharpTime(request.getStartTime()), Duration.ofMinutes(60)),
                request.getCancelInitiator()
        );
    }

    public RescheduleSystemCanceledCommand translateToRescheduleSystemCanceled(RescheduleSystemCanceledRequest request, String appointmentId) {
        return new RescheduleSystemCanceledCommand(
                new AppointmentId(appointmentId),
                new TimeSlot(request.getAppointmentDate(), new SharpTime(request.getStartTime()), Duration.ofMinutes(60))
        );
    }
    public AddUnavailabilityCommand translateToAddUnavailability(AddUnavailabilityRequest request, String doctorId) {
        return new AddUnavailabilityCommand(
                new DoctorId(doctorId),
                new TimePeriod(request.getStartTime(), request.getEndTime())
        );
    }
    public ChangeScheduleCommand translateToChangeSchedule(ChangeScheduleRequest request, String doctorId) {
        return new ChangeScheduleCommand(
                new DoctorId(doctorId),
                request.getValidFrom(),
                toWeeklySchedule(request)
        );
    }
    private WeeklySchedule toWeeklySchedule(ChangeScheduleRequest request) {
        Map<DayOfWeek, List<WorkingShift>> domainShifts = new EnumMap<>(DayOfWeek.class);

        for (Map.Entry<DayOfWeek, List<ChangeScheduleRequest.ShiftDTO>> entry : request.getShifts().entrySet()) {
            DayOfWeek day = entry.getKey();
            List<ChangeScheduleRequest.ShiftDTO> dailyShiftDtos = entry.getValue();


            List<WorkingShift> dayShifts = dailyShiftDtos.stream()
                    .map(dto -> new WorkingShift(
                            new SharpTime(dto.getStartTime()),
                            new SharpTime(dto.getEndTime())
                    ))
                    .collect(Collectors.toList());

            domainShifts.put(day, dayShifts);
        }

        return new WeeklySchedule(domainShifts);
    }

}
