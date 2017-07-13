package com.aniov.model.dto;

import com.aniov.model.dto.validators.EmailIsUnique;
import com.aniov.model.dto.validators.EmailIsValid;
import com.aniov.model.dto.validators.MatchFields;
import com.aniov.model.dto.validators.UserNameIsUnique;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Size;

/**
 * User Registration form dto
 */
@Data
@MatchFields(first = "plainPassword", second = "repeatPlainPassword", message = "passwords don't match")
public class UserRegisterDTO {

    @NotBlank
    @Size(min = 5, max = 30)
    @UserNameIsUnique
    private String username;

    @NotBlank
    @EmailIsUnique
    @EmailIsValid
    @Size(min = 5, max = 50, message = "Email must be between 5 and 50 characters long")
    private String email;

    @NotBlank
    private String plainPassword;

    @NotBlank
    private String repeatPlainPassword;
}
