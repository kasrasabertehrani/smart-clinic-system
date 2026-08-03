package com.smartclinicsystem.infrastructure.adapters.out.persistence.mapper;

import com.smartclinicsystem.domain.AppointmentCalendar;
import com.smartclinicsystem.domain.vo.*;
import com.smartclinicsystem.infrastructure.adapters.out.persistence.JpaEntities.AppointmentCalendarEntity;
import com.smartclinicsystem.infrastructure.adapters.out.persistence.JpaEntities.EffectiveScheduleEmbeddable;
import com.smartclinicsystem.infrastructure.adapters.out.persistence.JpaEntities.EffectiveScheduleEmbeddable.ShiftJson;
import com.smartclinicsystem.infrastructure.adapters.out.persistence.JpaEntities.UnavailabilityEmbeddable;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class AppointmentCalendarMapper {


    public AppointmentCalendarEntity toEntity(AppointmentCalendar domain) {
        if (domain == null) return null;

        AppointmentCalendarEntity entity = new AppointmentCalendarEntity(domain.getDoctorId().value());


        domain.getUnavailabilities().forEach(u -> {
            entity.addUnavailability(new UnavailabilityEmbeddable(
                    u.unavailabilityPeriod().startTime(),
                    u.unavailabilityPeriod().endTime()
            ));
        });

        domain.getEffectiveSchedules().forEach(schedule -> {
            Map<DayOfWeek, List<ShiftJson>> jsonMap = new EnumMap<>(DayOfWeek.class);

            schedule.schedule().schedule().forEach((day, shifts) -> {
                List<ShiftJson> jsonShifts = shifts.stream()
                        .map(s -> new ShiftJson(s.startTime().time(), s.endTime().time()))
                        .toList();
                jsonMap.put(day, jsonShifts);
            });

            entity.addEffectiveSchedule(new EffectiveScheduleEmbeddable(schedule.validFrom(), jsonMap));
        });

        return entity;
    }


    public AppointmentCalendar toCalendar(AppointmentCalendarEntity entity) {
        if (entity == null) return null;

        DoctorId doctorId = new DoctorId(entity.getDoctorId());


        List<Unavailability> unavailabilities = entity.getUnavailabilities().stream()
                .map(u -> new Unavailability(new TimePeriod(u.getStartTime(), u.getEndTime())))
                .collect(Collectors.toList());


        List<EffectiveSchedule> schedules = entity.getEffectiveSchedules().stream()
                .map(s -> {
                    Map<DayOfWeek, List<WorkingShift>> domainMap = new EnumMap<>(DayOfWeek.class);

                    s.getWeeklySchedule().forEach((day, jsonShifts) -> {
                        List<WorkingShift> domainShifts = jsonShifts.stream()
                                .map(json -> new WorkingShift(
                                        new SharpTime(json.startTime()),
                                        new SharpTime(json.endTime())
                                ))
                                .toList();
                        domainMap.put(day, domainShifts);
                    });

                    return new EffectiveSchedule(s.getValidFrom(), new WeeklySchedule(domainMap));
                })
                .collect(Collectors.toList());


        return new AppointmentCalendar(doctorId, schedules, unavailabilities);
    }
}
