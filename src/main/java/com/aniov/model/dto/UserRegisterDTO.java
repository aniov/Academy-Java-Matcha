package com.aniov.model.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Size;

/**
 * Registration form class
 */
@Data
public class UserRegisterDTO {

    @NotBlank
    @Size(min = 5, max = 30)
    private String username;

    @NotBlank
    @Email
    @Size(min = 3, max = 50)
    private String email;

    private String plainPassword;

    private String repeatPlainPassword;
}
