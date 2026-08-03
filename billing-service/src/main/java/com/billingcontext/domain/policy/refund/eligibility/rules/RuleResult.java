package com.billingcontext.domain.policy.refund.eligibility.rules;

import com.billingcontext.domain.policy.Evaluation;

public record RuleResult(Evaluation status, String reason) {

    public RuleResult {
        if(status == null){
            throw new RuleResultException("Status cannot be null");
        }
        if(status.equals(Evaluation.DENY) && (reason == null || reason.isEmpty())){
            throw new RuleResultException("reason must be provided when status is DENY");
        }
        if(status.equals(Evaluation.APPROVE) && reason != null) {
            throw new RuleResultException("reason must be null when status is APPROVE");
        }
        if(status.equals(Evaluation.NEUTRAL) && reason != null) {
            throw new RuleResultException("reason must be null when status is NEUTRAL");
        }
    }

    public static RuleResult forceApprove() {
        return new RuleResult(Evaluation.APPROVE, null);
    }

    public static RuleResult deny(String reason) {
        return new RuleResult(Evaluation.DENY, reason);
    }

    public static RuleResult neutral() {
        return new RuleResult(Evaluation.NEUTRAL, null);
    }


}
