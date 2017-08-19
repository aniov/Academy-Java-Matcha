package com.aniov.model.dto.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Height value validator interface
 */
@Target(value = ElementType.FIELD)
@Retention(RUNTIME)
@Constraint(validatedBy = HeightValueValidator.class)
@Documented
public @interface HeightValue {

    String message() default "height must be between 120 cm and 220 cm";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
