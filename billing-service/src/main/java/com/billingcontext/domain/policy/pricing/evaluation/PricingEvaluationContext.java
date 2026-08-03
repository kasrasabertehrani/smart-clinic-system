package com.billingcontext.domain.policy.pricing.evaluation;

import com.billingcontext.domain.shared.finance.Money;

import java.time.Clock;
import java.time.Duration;
import java.time.LocalDateTime;


public record PricingEvaluationContext (Money basePrice,
                                        LocalDateTime proformaCreationDate, LocalDateTime paymentDueDate, Clock clock) {

    public PricingEvaluationContext{
        LocalDateTime evaluationTime = LocalDateTime.now(clock);

        if (basePrice.isZero()){
            throw new PricingEvaluationContextException("Base price cannot be zero");
        }
        if (proformaCreationDate.isAfter(paymentDueDate)) {
            throw new PricingEvaluationContextException("creation date cannot be after payment due date");
        }
        if (proformaCreationDate.isAfter(evaluationTime)) {
            throw new PricingEvaluationContextException("Proforma creation date cannot be after now");
        }
        if(paymentDueDate.isBefore(evaluationTime)) {
            throw new PricingEvaluationContextException("Payment due date cannot be before now");
        }
    }
    public long minutesAfterProformaCreation(){
        return Duration.between(proformaCreationDate, LocalDateTime.now(clock)).toMinutes();
    }
    public long minutesToPaymentDueDate(){
        return Duration.between(LocalDateTime.now(clock), paymentDueDate).toMinutes();
    }
}
