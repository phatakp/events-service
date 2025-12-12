package com.kpevents.events_service.validators;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = FlatNumberValidator.class)
public @interface FlatNumber {
    String message() default "Invalid Flat Number";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
