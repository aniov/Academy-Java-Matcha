package com.aniov.model.dto.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * NameTextField validator interface
 */
@Target(value = ElementType.FIELD)
@Retention(RUNTIME)
@Constraint(validatedBy = NameTextFieldValidator.class)
@Documented
public @interface NameTextField {

    String message() default "Must be between 3 and 50 characters";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
