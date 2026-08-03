package com.smartclinicsystem.application.command;

import com.smartclinicsystem.domain.vo.DoctorId;
import com.smartclinicsystem.domain.vo.WeeklySchedule;

import java.time.LocalDate;

public record ChangeScheduleCommand(DoctorId doctorId, LocalDate validFrom, WeeklySchedule newSchedule) {
}
