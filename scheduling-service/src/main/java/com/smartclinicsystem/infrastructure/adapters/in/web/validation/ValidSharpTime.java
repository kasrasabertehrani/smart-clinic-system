package com.smartclinicsystem.infrastructure.adapters.in.web.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = {SharpTimeValidator.class, SharpDateTimeValidator.class})
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidSharpTime {


    String message() default "Time must be strictly in 15-minute intervals (e.g., 09:00, 09:15, 09:30, 09:45). Seconds are not allowed.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}