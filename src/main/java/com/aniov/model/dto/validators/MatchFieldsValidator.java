package com.aniov.model.dto.validators;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Create a Validator for two String fields
 * The fields should be identical to return true
 */
public class MatchFieldsValidator implements ConstraintValidator<MatchFields, Object> {

    private String firstField;
    private String secondField;

    @Override
    public void initialize(MatchFields constraintAnnotation) {

        firstField = constraintAnnotation.first();
        secondField = constraintAnnotation.second();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {

        try {
            BeanWrapper wrapper = PropertyAccessorFactory.forBeanPropertyAccess(value);
            String firstObj = (String) wrapper.getPropertyValue(firstField);
            String secondObj = (String) wrapper.getPropertyValue(secondField);

            return firstObj.length() > 0 && firstObj.equals(secondObj);

        } catch (final Exception ignore) {
            //ignore
        }
        return false;
    }
}
