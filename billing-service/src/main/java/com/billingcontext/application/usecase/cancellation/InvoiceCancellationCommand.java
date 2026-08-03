package com.billingcontext.application.usecase.cancellation;

import com.billingcontext.domain.invoice.AppointmentId;

import com.billingcontext.domain.policy.refund.evaluation.CancellationInitiator;


public record InvoiceCancellationCommand(
                                         CancellationInitiator initiator,
                                         long durationSinceBooking,
                                         long minutesUntilAppointment,
                                         AppointmentId appointmentId) {
}
