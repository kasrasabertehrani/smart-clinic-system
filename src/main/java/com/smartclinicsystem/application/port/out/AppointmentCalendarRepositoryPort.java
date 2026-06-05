package com.smartclinicsystem.application.port.out;

import com.smartclinicsystem.domain.AppointmentCalendar;
import com.smartclinicsystem.domain.vo.DoctorId;


public interface AppointmentCalendarRepositoryPort {

    AppointmentCalendar findByDoctorId(DoctorId doctorId);
    void save(AppointmentCalendar appointmentCalendar);

}
