package com.smartclinicsystem.application.port.out;

import com.smartclinicsystem.domain.Appointment;
import com.smartclinicsystem.domain.vo.AppointmentId;
import com.smartclinicsystem.domain.vo.DoctorId;
import com.smartclinicsystem.domain.vo.TimeSlot;

import java.util.List;

public interface AppointmentRepositoryPort {

    List<Appointment> findByDoctorId(DoctorId doctorId);
    void save(Appointment appointment);
    List<TimeSlot> findTimeSlotByDoctorId(DoctorId doctorId);
    Appointment findAppointmentId(AppointmentId appointmentId);
    void saveAll(List<Appointment> appointments);

}
