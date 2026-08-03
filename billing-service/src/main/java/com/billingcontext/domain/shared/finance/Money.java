package com.billingcontext.domain.shared.finance;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;

public record Money(BigDecimal amount, Currency currency) {


    public Money {
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new MoneyException("Amount cannot be negative");
        }
        amount = amount.setScale(4, RoundingMode.HALF_EVEN);

        if (currency == null) {
            throw new MoneyException("Currency cannot be null");
        }
    }


    public Money add(Money money) {
        if(isSameCurrency(money)) {
            return new Money(amount.add(money.amount()), currency);
        }
        else {
            throw new MoneyException("Cannot add different currency");
        }
    }

    public Money subtract(Money money) {
        if(isSameCurrency(money)) {
            if(isGreaterThanOrEqualTo(money)) {
                return new Money(amount.subtract(money.amount()), currency);
            }
            else {
                throw new MoneyException("Cannot subtract more than the current amount");
            }
        }
        else {
            throw new MoneyException("Cannot subtract different currency");
        }
    }
    public Money multiply(BigDecimal rate) {
        if (rate.compareTo(BigDecimal.ZERO) <= 0) {
            throw new MoneyException("Rate must be greater than zero");
        }
        return new Money(amount.multiply(rate), currency);
    }

    public boolean isSameCurrency(Money money) {
        return currency.equals(money.currency());
    }
    public boolean isGreaterThanOrEqualTo(Money money) {
        if(!isSameCurrency(money)) {
            throw new MoneyException("Cannot compare different currency");
        }
        return amount.compareTo(money.amount()) > 0;
    }
    public boolean isGreaterThan(Money money) {
        if(!isSameCurrency(money)) {
            throw new MoneyException("Cannot compare different currency");
        }
        return amount.compareTo(money.amount()) > 0;
    }
    public boolean isEqualTo(Money money) {
        return amount.compareTo(money.amount()) == 0;
    }
    public boolean isZero() {
        return amount.compareTo(BigDecimal.ZERO) == 0;
    }


    public Money convertTo(Currency targetCurrency, BigDecimal exchangeRate) {
        if (exchangeRate.compareTo(BigDecimal.ZERO) <= 0) {
            throw new MoneyException("Exchange rate must be positive");
        }
        BigDecimal newAmount = this.amount.multiply(exchangeRate);
        return new Money(newAmount, targetCurrency);
    }

    public long toCents() {
        return this.amount.multiply(new BigDecimal("100"))
                .setScale(0, RoundingMode.HALF_UP)
                .longValueExact();
    }

    public static Money of(double amount, String currency) {
        return new Money(BigDecimal.valueOf(amount), Currency.getInstance(currency));
    }
    public static Money of(long cents, String currency) {

        BigDecimal amountInDecimal = BigDecimal.valueOf(cents)
                .divide(new BigDecimal("100"), 2, java.math.RoundingMode.HALF_UP);


        return new Money(amountInDecimal, Currency.getInstance(currency));
    }
    public static Money of(BigDecimal amount, String currency) {
        return new Money(amount, Currency.getInstance(currency));
    }

    public static Money zero(Currency currency) {
        return new Money(BigDecimal.ZERO, currency);
    }
    public Money zero() {
        return new Money(BigDecimal.ZERO, this.currency);
    }
    public static Money moneyUSD(double amount){
        return new Money(new BigDecimal(amount), Currency.getInstance("USD"));
    }
    public static Money moneyEUR(double amount){
        return new Money(new BigDecimal(amount), Currency.getInstance("EUR"));
    }
}
