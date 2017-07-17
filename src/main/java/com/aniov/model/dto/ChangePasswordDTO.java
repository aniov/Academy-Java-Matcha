package com.aniov.model.dto;

import com.aniov.model.dto.validators.MatchFields;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

/**
 * User change password dto
 */

@Data
@MatchFields(first = "plainPassword", second = "repeatPlainPassword", message = "passwords don't match")
public class ChangePasswordDTO implements Serializable {

    @NotBlank
    private String plainPassword;

    @NotBlank
    private String repeatPlainPassword;
}
