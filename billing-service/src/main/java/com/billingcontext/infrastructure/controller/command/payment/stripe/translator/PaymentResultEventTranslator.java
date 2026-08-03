package com.billingcontext.infrastructure.controller.command.payment.stripe.translator;

import com.billingcontext.application.usecase.payment.finalize.TransactionResultCommand;
import com.billingcontext.domain.payment.PaymentStatus;
import com.billingcontext.domain.payment.TransactionId;
import com.billingcontext.domain.shared.finance.Money;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import org.springframework.stereotype.Component;


@Component
public class PaymentResultEventTranslator {

    public TransactionResultCommand translate(Event event) {


        PaymentIntent paymentIntent = (PaymentIntent) event
                .getDataObjectDeserializer()
                .getObject()
                .orElseThrow(() -> new IllegalStateException(
                        "Unable to deserialize PaymentIntent from event: "
                                + event.getId()
                ));

        PaymentStatus paymentStatus =
                "succeeded".equals(paymentIntent.getStatus())
                        ? PaymentStatus.SUCCESS
                        : PaymentStatus.FAILED;

        Money money = null;
        if (paymentStatus == PaymentStatus.SUCCESS) {
            money = Money.of(
                    paymentIntent.getAmount(),
                    paymentIntent.getCurrency().toUpperCase()
            );
        }

        TransactionId transactionId = new TransactionId(
                paymentIntent.getId()
        );


        String failureReason = null;
        if (paymentStatus == PaymentStatus.FAILED
                && paymentIntent.getLastPaymentError() != null) {
            failureReason = paymentIntent
                    .getLastPaymentError()
                    .getMessage();
        }

        return new TransactionResultCommand(
                transactionId,
                paymentStatus,
                money,
                failureReason
        );
    }
}