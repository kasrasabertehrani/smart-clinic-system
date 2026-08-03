package com.billingcontext.infrastructure.controller.command.payment.stripe.translator;

import com.billingcontext.application.usecase.payment.finalize.TransactionResultCommand;
import com.billingcontext.domain.payment.PaymentStatus;
import com.billingcontext.domain.payment.TransactionId;
import com.billingcontext.domain.shared.finance.Money;
import com.stripe.model.Event;
import com.stripe.model.Refund;
import com.stripe.model.StripeObject;
import org.springframework.stereotype.Component;



@Component
public class RefundResultEventTranslator {

    public TransactionResultCommand translate(Event event) {

        StripeObject stripeObject = event
                .getDataObjectDeserializer()
                .getObject()
                .orElseThrow(() -> new IllegalStateException(
                        "Could not deserialize event data: " + event.getId()
                ));

        if (!(stripeObject instanceof Refund refund)) {
            throw new IllegalStateException("Expected a Charge object for refund event, but got: "
                    + stripeObject.getClass().getSimpleName());
        }



        TransactionId refundId = new TransactionId(
                refund.getId()
        );

        PaymentStatus refundStatus = "succeeded".equals(refund.getStatus())
                ? PaymentStatus.SUCCESS
                : PaymentStatus.FAILED;


        Money money = null;
        if (refundStatus == PaymentStatus.SUCCESS) {
            money = Money.of(
                    refund.getAmount(),
                    refund.getCurrency().toUpperCase()
            );
        }

        String failureReason = null;
        if (refundStatus == PaymentStatus.FAILED
                && refund.getFailureReason() != null) {
            failureReason = refund.getFailureReason();
        }

        return new TransactionResultCommand(
                refundId,
                refundStatus,
                money,
                failureReason
        );
    }
}