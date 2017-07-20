package com.aniov.model.dto;

import com.aniov.model.dto.validators.EmailIsUnique;
import com.aniov.model.dto.validators.EmailIsValid;
import com.aniov.model.dto.validators.MatchFields;
import com.aniov.model.dto.validators.UserNameIsUnique;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * User Registration form dto
 */
@Data
@MatchFields(first = "plainPassword", second = "repeatPlainPassword", message = "passwords don't match")
public class UserRegisterDTO implements Serializable{

    @Size(min = 5, max = 30, message = "username should be between 5 and 30 characters")
    @UserNameIsUnique
    private String username;

    @EmailIsUnique
    @EmailIsValid
    @Size(min = 5, max = 50, message = "email must be between 5 and 50 characters long")
    private String email;

    @Size(min = 5, max = 50, message = "password should be between 5 and 50 characters")
    private String plainPassword;

    private String repeatPlainPassword;
}
