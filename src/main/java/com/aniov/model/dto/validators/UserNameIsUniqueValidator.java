package com.aniov.model.dto.validators;

import com.aniov.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Create a Validator to check if the username String field provided is unique
 */

public class UserNameIsUniqueValidator implements ConstraintValidator<UserNameIsUnique, String> {

    @Autowired
    private UserService userService;

    @Override
    public void initialize(UserNameIsUnique constraintAnnotation) {
    }

    @Override
    public boolean isValid(String userName, ConstraintValidatorContext context) {

        return (userService.findUserByUserNameIgnoreCase(userName) == null);
    }
}
