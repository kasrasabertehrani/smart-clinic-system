package com.billingcontext.infrastructure.gateway.stripe;

import com.billingcontext.application.usecase.payment.initiate.checkout.PaymentGatewayPort;
import com.billingcontext.domain.payment.TransactionId;
import com.billingcontext.domain.shared.finance.Money;
import com.billingcontext.infrastructure.dto.response.PaymentIntentResponse;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;

import com.stripe.param.PaymentIntentCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;



@Component
public class ProcessPaymentService implements PaymentGatewayPort {
    @Value("${stripe.api.key}")
    private String stripeApiKey;

    @Override
    public PaymentIntentResponse processPayment(Money amount) {

        try {
            Stripe.apiKey = stripeApiKey;
            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                        .setAmount(amount.toCents())
                        .setCurrency(amount.currency().toString().toLowerCase())
                        .addPaymentMethodType("card")
                        .build();

            PaymentIntent intent = PaymentIntent.create(params);
            return new PaymentIntentResponse(
                    intent.getClientSecret(),
                    new TransactionId(intent.getId())
            );

        } catch (StripeException e) {
            throw new GatewayConnectionException("Failed to process payment: " + e.getMessage());
        }

    }
}
