package com.aniov.model.dto;

import com.aniov.model.dto.validators.MatchFields;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * User change password dto
 */

@Data
@MatchFields(first = "plainPassword", second = "repeatPlainPassword", message = "Passwords don't match")
public class ChangePasswordDTO implements Serializable {

    @Size(min = 5, max = 50, message = "Password should be between 5 and 50 characters")
    private String plainPassword;

    private String repeatPlainPassword;
}
