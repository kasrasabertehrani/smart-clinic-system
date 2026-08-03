package com.billingcontext.infrastructure.config.policy.refund;

import com.billingcontext.domain.policy.refund.RefundPolicyService;
import com.billingcontext.domain.policy.refund.amount.RefundStrategyService;
import com.billingcontext.domain.policy.refund.amount.strategy.FullRefundStrategy;
import com.billingcontext.domain.policy.refund.amount.strategy.LatePenaltyDeductibleStrategy;
import com.billingcontext.domain.policy.refund.amount.strategy.RefundCalculationStrategy;
import com.billingcontext.domain.policy.refund.eligibility.RefundEligibilityService;
import com.billingcontext.domain.policy.refund.eligibility.rules.BookingGracePeriodSpecification;
import com.billingcontext.domain.policy.refund.eligibility.rules.ClinicInitiatedSpecification;
import com.billingcontext.domain.policy.refund.eligibility.rules.MinimumNoticeSpecification;
import com.billingcontext.domain.policy.refund.eligibility.rules.RefundEligibilityRule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class RefundPolicyConfig {

    @Bean
    public RefundPolicyService refundPolicyService() {
        List<RefundEligibilityRule> activeClinicRules = List.of(
                new BookingGracePeriodSpecification(),
                new ClinicInitiatedSpecification(),
                new MinimumNoticeSpecification()
        );
        RefundEligibilityService refundEligibilityService = new RefundEligibilityService(activeClinicRules);

        List<RefundCalculationStrategy>  refundCalculationStrategies = List.of(
               new LatePenaltyDeductibleStrategy(),
               new FullRefundStrategy()
        );

        RefundStrategyService refundStrategyService = new RefundStrategyService(refundCalculationStrategies);
        return new RefundPolicyService(refundStrategyService, refundEligibilityService);
    }
}
