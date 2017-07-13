package com.aniov.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * A class who contains a token to be sent to front end
 */

@Data
@AllArgsConstructor
public class JwtLoginResponseDTO {

    private String token;
}
