package com.billingcontext.infrastructure.controller.command.payment.stripe;

import com.billingcontext.application.usecase.payment.finalize.TransactionResultCommand;
import com.billingcontext.infrastructure.controller.command.payment.stripe.translator.PaymentResultEventTranslator;
import com.billingcontext.infrastructure.controller.command.payment.stripe.translator.RefundResultEventTranslator;
import com.stripe.model.Event;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class StripeTranslatorDispatcher {

    private final PaymentResultEventTranslator paymentTranslator;
    private final RefundResultEventTranslator refundTranslator;

    public Optional<TransactionResultCommand> dispatch(Event event) {

        return switch (event.getType()) {
            case "payment_intent.succeeded", "payment_intent.payment_failed" ->
                    Optional.of(paymentTranslator.translate(event));

            case "charge.refund.updated" ->
                    Optional.of(refundTranslator.translate(event));

            default -> {
                log.debug("Unhandled Stripe event type: {}", event.getType());
                yield Optional.empty();
            }

        };
    }
}
