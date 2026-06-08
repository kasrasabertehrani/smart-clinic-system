package com.smartclinicsystem.infrastructure.adapters.in.web.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

public class SharpDateTimeValidator implements ConstraintValidator<ValidSharpTime, LocalDateTime> {

    @Override
    public void initialize(ValidSharpTime constraintAnnotation) {
    }

    @Override
    public boolean isValid(LocalDateTime value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }


        return (value.getMinute() % 15 == 0)
                && value.getSecond() == 0
                && value.getNano() == 0;
    }
}