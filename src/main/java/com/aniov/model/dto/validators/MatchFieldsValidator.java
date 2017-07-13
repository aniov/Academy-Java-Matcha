package com.aniov.model.dto.validators;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Created by Marius on 12/9/2016.
 */

/**
 * Create a Validator for two String fields
 * The fields should be identical to return true
 */
public class MatchFieldsValidator implements ConstraintValidator<MatchFields, Object> {

    private String firstFieldName;
    private String secondFieldName;

    @Override
    public void initialize(MatchFields constraintAnnotation) {

        firstFieldName = constraintAnnotation.first();
        secondFieldName = constraintAnnotation.second();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {

        try {
            BeanWrapper wrapper = PropertyAccessorFactory.forBeanPropertyAccess(value);
            Object firstObj = wrapper.getPropertyValue(firstFieldName);
            Object secondObj = wrapper.getPropertyValue(secondFieldName);

            return firstObj == null && secondObj == null || firstObj != null && firstObj.equals(secondObj);

        } catch (final Exception ignore) {
            //ignore
        }
        return false;
    }
}
