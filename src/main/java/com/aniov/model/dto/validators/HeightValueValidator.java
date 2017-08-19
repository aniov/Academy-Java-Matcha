package com.aniov.model.dto.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * A validator for user height
 */
public class HeightValueValidator implements ConstraintValidator<HeightValue, Integer> {

    @Override
    public void initialize(HeightValue constraintAnnotation) {

    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        return value == null || value >= 120 && value <= 220;
    }
}
