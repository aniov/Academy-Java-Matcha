package com.aniov.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

/**
 * A class who contains a token to be sent to front end
 */

@Getter
@AllArgsConstructor
public class JwtLoginResponseDTO implements Serializable {

    private String token;
}
