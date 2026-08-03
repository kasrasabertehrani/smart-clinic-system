package com.billingcontext.infrastructure.gateway.stripe;

public class GatewayConnectionException extends RuntimeException {
    public GatewayConnectionException(String message) {
        super(message);
    }
}
