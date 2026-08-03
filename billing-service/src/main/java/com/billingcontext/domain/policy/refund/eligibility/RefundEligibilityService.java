package com.billingcontext.domain.policy.refund.eligibility;


import com.billingcontext.domain.policy.refund.evaluation.RefundEvaluationContext;
import com.billingcontext.domain.policy.refund.eligibility.rules.RefundEligibilityRule;
import com.billingcontext.domain.policy.refund.eligibility.rules.RuleOrder;
import com.billingcontext.domain.policy.refund.eligibility.rules.RuleResult;
import com.billingcontext.domain.policy.Evaluation;
import lombok.Getter;


import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Getter
public class RefundEligibilityService {

    private final List<RefundEligibilityRule> vetoSpecs;
    private final List<RefundEligibilityRule> overrideSpecs;
    private final List<RefundEligibilityRule> standardSpecs;

    public RefundEligibilityService(
            List<RefundEligibilityRule> allSpecifications) {

        this.vetoSpecs = allSpecifications.stream()
                .filter(s -> s.order().tier() == RuleOrder.RuleTier.VETO)
                .sorted(Comparator.comparing(RefundEligibilityRule::order))
                .toList();

        this.overrideSpecs = allSpecifications.stream()
                .filter(s -> s.order().tier() == RuleOrder.RuleTier.OVERRIDE)
                .sorted(Comparator.comparing(RefundEligibilityRule::order))
                .toList();

        this.standardSpecs = allSpecifications.stream()
                .filter(s -> s.order().tier() == RuleOrder.RuleTier.STANDARD)
                .sorted(Comparator.comparing(RefundEligibilityRule::order))
                .toList();

        if(allSpecifications.isEmpty()) {
            throw new RefundEligibilityServiceException("No specifications found");
        }
    }

    public Optional<String> evaluate(
            RefundEvaluationContext context) {


        for (RefundEligibilityRule spec : vetoSpecs) {
            RuleResult result = spec.evaluate(context);
            if (result.status() == Evaluation.DENY) {
                return Optional.of(result.reason());
            }
        }


        for (RefundEligibilityRule spec : overrideSpecs) {
            RuleResult result = spec.evaluate(context);
            if (result.status() == Evaluation.APPROVE) {
                return Optional.empty();
            }
        }


        for (RefundEligibilityRule spec : standardSpecs) {
            RuleResult result = spec.evaluate(context);
            if (result.status() == Evaluation.DENY) {
                return Optional.of(result.reason());
            }
        }

        return Optional.empty();
    }
}