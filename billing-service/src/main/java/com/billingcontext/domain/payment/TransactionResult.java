package com.billingcontext.domain.payment;

import com.billingcontext.domain.shared.finance.Money;


public record TransactionResult(Payment.PaymentType paymentType,
                            Money amountPaid,
                            PaymentStatus paymentStatus,
                            String failureReason) {
    public TransactionResult{
        if (paymentStatus == PaymentStatus.PENDING) {
            throw new PaymentResultException("Payment result cannot be PENDING");
        }

        if(paymentStatus == PaymentStatus.SUCCESS && amountPaid == null){
            throw new PaymentResultException("Amount paid cannot be null for successful payment");
        }
        if(paymentStatus == PaymentStatus.SUCCESS && failureReason != null){
            throw new PaymentResultException("Successful payment cannot have failure reason");
        }
        if(paymentStatus == PaymentStatus.FAILED && failureReason == null){
            throw new PaymentResultException("Failed payment must have failure reason");
        }
        if(paymentStatus == PaymentStatus.FAILED && amountPaid != null){
            throw new PaymentResultException("Failed payment cannot paid amount");
        }
    }

    public boolean isPayment(){
        return paymentType == Payment.PaymentType.PAYMENT_DEDUCTION;
    }
    public boolean isSuccessful(){
        return paymentStatus == PaymentStatus.SUCCESS;
    }
}
