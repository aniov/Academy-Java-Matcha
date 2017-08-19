package com.aniov.model.dto.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * A validator for string input fields
 */
public class NameTextFieldValidator implements ConstraintValidator<NameTextField, String> {

    @Override
    public void initialize(NameTextField constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        return value == null || value.length() == 0 || value.length() >= 3 && value.length() <= 50;
    }
}
