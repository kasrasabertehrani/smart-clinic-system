package com.billingcontext.infrastructure.config.policy.payment;

import com.billingcontext.domain.policy.pricing.PricingPolicyService;
import com.billingcontext.domain.policy.pricing.discount.DiscountPolicyService;
import com.billingcontext.domain.policy.pricing.discount.rules.DiscountRule;
import com.billingcontext.domain.policy.pricing.discount.rules.EarlyClearanceDiscountRule;
import com.billingcontext.domain.policy.pricing.discount.rules.ImmediateSettlementDiscountRule;
import com.billingcontext.domain.policy.pricing.discount.rules.PromptPaymentDiscountRule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class PricingPolicyConfig {

    @Bean
    public PricingPolicyService pricingPolicyService() {

        List<DiscountRule> activeClinicRules = List.of(
                new EarlyClearanceDiscountRule(),
                new ImmediateSettlementDiscountRule(),
                new PromptPaymentDiscountRule()
        );
        DiscountPolicyService discountPolicyService = new DiscountPolicyService(activeClinicRules);
        return new PricingPolicyService(discountPolicyService);
    }
}
