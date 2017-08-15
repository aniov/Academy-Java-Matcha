package com.aniov.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class IsOnlineSocketDTO implements Serializable {

    private String username;
    private boolean online;
}
