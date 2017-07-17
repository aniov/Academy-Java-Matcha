package com.aniov.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * Generic message sent to front-end as Json
 */

@Data
@AllArgsConstructor
public class GenericResponseDTO implements Serializable {

    private String message;
    private String error;
}
