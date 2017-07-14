package com.aniov.model.dto.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * A username Validator interface
 */

@Target(value = ElementType.FIELD)
@Retention(RUNTIME)
@Constraint(validatedBy = UserNameIsUniqueValidator.class)
@Documented
public @interface UserNameIsUnique {

    String message() default "This username already exists";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
