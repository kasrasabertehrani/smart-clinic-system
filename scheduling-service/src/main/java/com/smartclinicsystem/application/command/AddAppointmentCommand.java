package com.smartclinicsystem.application.command;

import com.smartclinicsystem.domain.vo.DoctorId;
import com.smartclinicsystem.domain.vo.PatientId;
import com.smartclinicsystem.domain.vo.TimeSlot;

public record AddAppointmentCommand(
        DoctorId doctorId,
        PatientId patientId,
        TimeSlot timeSlot
) {
}
