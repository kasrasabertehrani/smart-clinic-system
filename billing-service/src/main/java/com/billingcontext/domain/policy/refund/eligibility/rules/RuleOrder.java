package com.billingcontext.domain.policy.refund.eligibility.rules;

public record RuleOrder(RuleTier tier, int priority)
        implements Comparable<RuleOrder> {

    public RuleOrder {
        if(tier == null){
            throw new RuleOrderException("tier is null");
        }
        if (priority < 1) {
            throw new RuleOrderException(
                    "Priority must be 1 or greater"
            );
        }
    }


    public static RuleOrder veto(int priority) {
        return new RuleOrder(RuleTier.VETO, priority);
    }

    public static RuleOrder override(int priority) {
        return new RuleOrder(RuleTier.OVERRIDE, priority);
    }

    public static RuleOrder standard(int priority) {
        return new RuleOrder(RuleTier.STANDARD, priority);
    }


    @Override
    public int compareTo(RuleOrder other) {
        int tierComparison = this.tier.ordinal() - other.tier.ordinal();
        if (tierComparison != 0) return tierComparison;
        return Integer.compare(this.priority, other.priority);
    }

    public enum RuleTier {
        VETO,
        OVERRIDE,
        STANDARD
    }
}
