package com.billingcontext.infrastructure.gateway.stripe;

import com.billingcontext.application.usecase.payment.initiate.refund.RefundGatewayPort;
import com.billingcontext.domain.payment.TransactionId;
import com.billingcontext.domain.shared.finance.Money;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Refund;
import com.stripe.param.RefundCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;



@Component
public class RefundProcessService implements RefundGatewayPort {
    @Value("${stripe.api.key}")
    private String stripeApiKey;

    @Override
    public TransactionId processRefund(Money refundAmount, TransactionId paymentIntentId) {

        Stripe.apiKey = stripeApiKey;

            try {

                RefundCreateParams params = RefundCreateParams.builder()
                        .setPaymentIntent(paymentIntentId.value())
                        .setAmount(refundAmount.toCents())
                        .build();

                Refund refundIntent = Refund.create(params);
                return new TransactionId(refundIntent.getId());

            } catch (StripeException e) {
                throw new GatewayConnectionException("Failed to process refund: " + e.getMessage());
            }

    }
}

