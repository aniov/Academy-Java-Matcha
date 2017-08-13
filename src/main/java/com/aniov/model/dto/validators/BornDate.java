package com.aniov.model.dto.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Born date validator interface
 */
@Target(value = ElementType.FIELD)
@Retention(RUNTIME)
@Constraint(validatedBy = BornDateValidator.class)
@Documented
public @interface BornDate {

    String message() default "User isn't old enough (18 years old)";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
