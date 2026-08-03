package com.billingcontext.infrastructure.controller.command.payment.stripe;

import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.net.Webhook;
import org.springframework.stereotype.Component;

@Component
public class StripeSignatureVerifier {

    public Event verifyAndConstructEvent(String payload, String sigHeader, String endPointSecret) {
        try {
            return Webhook.constructEvent(payload, sigHeader, endPointSecret);
        } catch (SignatureVerificationException e) {
            throw new InvalidWebhookSignatureException("Error verifying Stripe webhook signature: " + e.getMessage());
        }
    }
}
