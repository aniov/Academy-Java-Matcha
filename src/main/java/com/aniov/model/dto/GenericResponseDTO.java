package com.aniov.model.dto;

import lombok.Getter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Generic message sent to front-end as Json
 */

public class GenericResponseDTO implements Serializable {

    private String message;
    @Getter
    private List<String> messages = new ArrayList<>();

    public GenericResponseDTO(String message) {
        this.message = message;
        this.messages.add(message);
    }

    public GenericResponseDTO(List<String> messages) {
        this.messages = messages;
    }
}
