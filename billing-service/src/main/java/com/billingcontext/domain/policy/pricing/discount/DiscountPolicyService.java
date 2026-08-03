package com.billingcontext.domain.policy.pricing.discount;


import com.billingcontext.domain.policy.pricing.discount.rules.DiscountRule;
import com.billingcontext.domain.policy.pricing.evaluation.PricingEvaluationContext;
import com.billingcontext.domain.policy.pricing.discount.rules.DiscountRuleResult;
import com.billingcontext.domain.policy.pricing.discount.rules.DiscountType;
import com.billingcontext.domain.shared.finance.Percentage;
import lombok.Getter;

import java.util.Comparator;
import java.util.List;

@Getter
public class DiscountPolicyService {

    private final List<DiscountRule> stackableRules;
    private final List<DiscountRule> exclusiveRules;

    public DiscountPolicyService(List<DiscountRule> allRules) {
        this.stackableRules = allRules.stream()
                .filter(r -> r.type() == DiscountType.STACKABLE)
                .toList();

        this.exclusiveRules = allRules.stream()
                .filter(r -> r.type() == DiscountType.EXCLUSIVE)
                .toList();
    }

    public Percentage evaluate(PricingEvaluationContext context) {


        Percentage stackedDiscount = stackableRules.stream()
                .map(rule -> rule.evaluate(context))
                .filter(DiscountRuleResult::isApplicable)
                .map(DiscountRuleResult::percentage)
                .reduce(Percentage.zero(), Percentage::add);


        Percentage bestExclusiveDiscount = exclusiveRules.stream()
                .map(rule -> rule.evaluate(context))
                .filter(DiscountRuleResult::isApplicable)
                .map(DiscountRuleResult::percentage)
                .max(Comparator.comparing(Percentage::value))
                .orElse(Percentage.zero());



        return stackedDiscount.isGreaterThan(bestExclusiveDiscount)
                ? stackedDiscount
                : bestExclusiveDiscount;
    }
}
