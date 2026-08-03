package com.billingcontext.infrastructure.controller.command.payment.stripe;

public class InvalidWebhookSignatureException extends RuntimeException {
    public InvalidWebhookSignatureException(String message) {
        super(message);
    }
}
