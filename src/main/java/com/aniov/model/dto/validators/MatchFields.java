package com.aniov.model.dto.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by Marius on 12/9/2016.
 */
@Target(TYPE)
@Retention(RUNTIME)
@Constraint(validatedBy = MatchFieldsValidator.class)
@Documented
public @interface MatchFields {

    String first();

    String second();

    String message() default "The fields don't match";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
