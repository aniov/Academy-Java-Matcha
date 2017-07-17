package com.aniov.model.dto;

import com.aniov.model.dto.validators.EmailIsValid;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Size;

/**
 * Reset password dto
 */
@Data
public class ResetPasswordDTO {

    @NotBlank
    @EmailIsValid
    @Size(min = 5, max = 50, message = "Email must be between 5 and 50 characters long")
    private String email;
}
