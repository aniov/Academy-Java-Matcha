package com.aniov.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class ProfileLikeSocketDTO implements Serializable {

    private String username;
    private boolean like;
}
