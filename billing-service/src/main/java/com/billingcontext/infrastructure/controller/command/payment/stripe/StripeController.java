package com.billingcontext.infrastructure.controller.command.payment.stripe;

import com.billingcontext.application.usecase.payment.finalize.FinalizeTransactionUseCase;
import com.stripe.model.Event;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class StripeController {

    private final StripeTranslatorDispatcher dispatcher;
    private final StripeSignatureVerifier verifier;
    private final FinalizeTransactionUseCase finalizeTransactionUseCase;

    @Value("${stripe.payment.endPoint.key}")
    private String paymentEndPointSecret;

    @PostMapping("/stripe/webhook/payments")
    public ResponseEntity<Void> handleStripeTransactionResponse(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String sigHeader) {

        Event event = verifier.verifyAndConstructEvent(payload, sigHeader, paymentEndPointSecret);
        dispatcher.dispatch(event).ifPresent(finalizeTransactionUseCase::finalizeTransactionForLedger);
        return ResponseEntity.ok().build();
    }
}
