package com.aniov.model.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.Column;
import javax.validation.constraints.Size;

/**
 * DTO Login class
 */

@Data
public class JwtLoginDTO {

    @NotBlank
    @Size(min = 5, max = 30)
    private String username;

    @NotBlank
    @Size(min = 6, max = 50)
    private String plainPassword;
}
