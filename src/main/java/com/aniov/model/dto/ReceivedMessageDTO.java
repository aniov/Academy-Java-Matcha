package com.aniov.model.dto;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

@Data
public class ReceivedMessageDTO implements Serializable {

    @NotBlank
    private String message;
}
