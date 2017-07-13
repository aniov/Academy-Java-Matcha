package com.aniov.model.dto.validators;

import javax.validation.Constraint;
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
}
