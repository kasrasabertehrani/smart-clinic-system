package com.billingcontext.application.usecase.proforma;

import com.billingcontext.domain.invoice.AppointmentId;
import com.billingcontext.domain.shared.id.DoctorId;
import com.billingcontext.domain.shared.id.PatientId;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record IssueProformaCommand(AppointmentId appointmentId,
                                   DoctorId doctorId,
                                   PatientId patientId,
                                   LocalDateTime expirationDate,
                                   BigDecimal appointmentDuration) {}
