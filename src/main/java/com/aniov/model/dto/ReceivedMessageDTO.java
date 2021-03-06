package com.aniov.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class ReceivedMessageDTO implements Serializable {

    private String username;

    @NotBlank
    private String message;
}
