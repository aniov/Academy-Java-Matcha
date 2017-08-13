package com.aniov.model.dto.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Calendar;
import java.util.Date;

/**
 * A validator for user born date
 */
public class BornDateValidator implements ConstraintValidator<BornDate, Date> {

    @Override
    public void initialize(BornDate constraintAnnotation) {

    }

    @Override
    public boolean isValid(Date bornDate, ConstraintValidatorContext context) {

        if (bornDate == null)
            return true;

        //User must have at least 18 years old
        Calendar atLeast = Calendar.getInstance();
        atLeast.add(Calendar.YEAR, -18);
        atLeast.add(Calendar.DATE, 1);

        Calendar userBorn = Calendar.getInstance();
        userBorn.setTime(bornDate);

        return userBorn.getTime().before(atLeast.getTime());
    }
}
