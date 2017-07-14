package com.aniov.model.dto.validators;

import com.aniov.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Created by Marius on 12/10/2016.
 */

/**
 * Create a Validator to check if the email String field provided is unique
 */
public class EmailIsUniqueValidator implements ConstraintValidator<EmailIsUnique, String> {

    @Autowired
    private UserService userService;

    @Override
    public void initialize(EmailIsUnique constraintAnnotation) {
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        return (userService.findAccountByEmail(email) == null);
    }
}
