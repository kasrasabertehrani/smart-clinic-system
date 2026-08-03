package com.billingcontext.domain.policy.refund.evaluation;


import com.billingcontext.domain.shared.finance.Money;

public record RefundEvaluationContext(
        Money totalAmount,
        CancellationInitiator initiator,
        long durationSinceBooking,
        long minutesUntilAppointment
) {
    public RefundEvaluationContext {
      if (initiator == null) {
          throw new RefundEvaluationContextException("initiator cannot be null");
      }
      if (durationSinceBooking < 0) {
          throw new RefundEvaluationContextException("durationSinceBooking cannot be negative");
      }
      if (minutesUntilAppointment < 0) {
          throw new RefundEvaluationContextException("minutesUntilAppointment cannot be negative");
      }
    }
    public boolean isCanceledBySystemAutomation() {
        return initiator == CancellationInitiator.SYSTEM_AUTOMATION;
    }
    public boolean isCanceledByClinicStaff() {
        return initiator == CancellationInitiator.CLINIC_RECEPTION;
    }
    public boolean isCanceledByPatient() {
        return initiator == CancellationInitiator.PATIENT;
    }
}