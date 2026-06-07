package com.smartclinicsystem.infrastructure.adapters.in.web.DTO.response;

import com.smartclinicsystem.domain.Appointment;
import com.smartclinicsystem.domain.vo.EffectiveSchedule;
import com.smartclinicsystem.domain.vo.WorkingShift;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;


public record ChangeScheduleResponse(
        LocalDate validFrom,
        Map<DayOfWeek, List<ShiftResponse>> newWeeklySchedule,
        List<CancelAppointmentResponse> canceledAppointments
) {


    public record ShiftResponse(LocalTime startTime, LocalTime endTime) {

        public static ShiftResponse from(WorkingShift shift) {
            return new ShiftResponse(shift.startTime().time(), shift.endTime().time());
        }
    }

    public static ChangeScheduleResponse from(
            EffectiveSchedule newSchedule,
            List<Appointment> canceledAppointments) {


        List<CancelAppointmentResponse> canceledList = canceledAppointments.stream()
                .map(CancelAppointmentResponse::from)
                .toList();


        Map<DayOfWeek, List<ShiftResponse>> scheduleMap = new EnumMap<>(DayOfWeek.class);

        for (Map.Entry<DayOfWeek, List<WorkingShift>> entry : newSchedule.schedule().schedule().entrySet()) {
            List<ShiftResponse> shiftResponses = entry.getValue().stream()
                    .map(ShiftResponse::from)
                    .toList();
            scheduleMap.put(entry.getKey(), shiftResponses);
        }

        return new ChangeScheduleResponse(
                newSchedule.validFrom(),
                scheduleMap,
                canceledList
        );
    }
}