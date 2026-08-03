package com.billingcontext.domain.shared.finance;

import java.math.BigDecimal;
import java.math.RoundingMode;

public record Percentage(BigDecimal value) {

    public Percentage {
        if (value.compareTo(BigDecimal.ZERO) < 0) {
            throw new PercentageException(
                    "Percentage cannot be negative"
            );
        }
        if (value.compareTo(BigDecimal.valueOf(100)) > 0) {
            throw new PercentageException(
                    "Percentage cannot exceed 100"
            );
        }
        value = value.setScale(2, RoundingMode.HALF_EVEN);
    }

    public static Percentage of(double value) {
        return new Percentage(BigDecimal.valueOf(value));
    }


    public static Percentage zero() {
        return new Percentage(BigDecimal.ZERO);
    }

    public boolean isHundred() {
        return value.compareTo(BigDecimal.valueOf(100)) == 0;
    }
    public boolean isZero() {
        return value.compareTo(BigDecimal.ZERO) == 0;
    }

    public Percentage add(Percentage other) {
        BigDecimal combined = this.value.add(other.value);
        if (combined.compareTo(BigDecimal.valueOf(100)) > 0) {
            return new Percentage(BigDecimal.valueOf(100));
        }
        return new Percentage(combined);
    }

    public boolean isGreaterThan(Percentage other) {
        return this.value.compareTo(other.value) > 0;
    }

    public Money applyDiscountTo(Money price) {
        BigDecimal discountAmount = price.amount()
                .multiply(value)
                .divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_EVEN);
        return new Money(
                price.amount().subtract(discountAmount),
                price.currency()
        );
    }

    public Money applyTaxTo(Money price) {
        BigDecimal taxAmount = price.amount()
                .multiply(value)
                .divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_EVEN);
        return new Money(
                price.amount().add(taxAmount),
                price.currency()
        );
    }
}
