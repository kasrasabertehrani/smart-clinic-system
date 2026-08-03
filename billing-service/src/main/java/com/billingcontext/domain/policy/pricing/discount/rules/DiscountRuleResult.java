package com.billingcontext.domain.policy.pricing.discount.rules;

import com.billingcontext.domain.policy.Evaluation;
import com.billingcontext.domain.shared.finance.Percentage;



public record DiscountRuleResult(Evaluation status, Percentage percentage, String reason) {

    public DiscountRuleResult{
        if(status == null) {
            throw new DiscountRuleResultException("Status cannot be null");
        }
        if(status == Evaluation.NEUTRAL){
            throw new DiscountRuleResultException("Evaluation result cannot be NEUTRAL");
        }
        if(status == Evaluation.APPROVE && percentage == null){
            throw new DiscountRuleResultException("Percentage must be provided when status is APPROVE");
        }
        if(status == Evaluation.APPROVE && reason != null){
            throw new DiscountRuleResultException("Reason must not be provided when status is APPROVE");
        }
        if(status == Evaluation.DENY && percentage != null){
            throw new DiscountRuleResultException("Percentage must not be provided when status is DENY");
        }
        if(status == Evaluation.DENY && reason == null){
            throw new DiscountRuleResultException("Reason must be provided when status is DENY");
        }
    }
    public static DiscountRuleResult applicable(Percentage percentage) {
        return new DiscountRuleResult(
                Evaluation.APPROVE, percentage, null
        );
    }

    public static DiscountRuleResult notApplicable(String reason) {
        return new DiscountRuleResult(
                Evaluation.DENY, null, reason
        );
    }

    public boolean isApplicable() {
        return status == Evaluation.APPROVE;
    }


}