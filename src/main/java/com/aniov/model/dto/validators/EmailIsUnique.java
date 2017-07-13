/**
 * Created by Marius on 12/10/2016.
 */
package com.aniov.model.dto.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;


/**
 * A email Validator interface
 */

@Target(value = ElementType.FIELD)
@Retention(RUNTIME)
@Constraint(validatedBy = EmailIsUniqueValidator.class)
@Documented
public @interface EmailIsUnique {

    String message() default "This email already exists";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
