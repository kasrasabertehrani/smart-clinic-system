package com.smartclinicsystem.application.command;

import com.smartclinicsystem.domain.vo.DoctorId;
import com.smartclinicsystem.domain.vo.TimePeriod;

public record AddUnavailabilityCommand(DoctorId doctorId, TimePeriod timePeriod) {
}
