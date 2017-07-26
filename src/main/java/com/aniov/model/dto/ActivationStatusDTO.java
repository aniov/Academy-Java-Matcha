package com.aniov.model.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class ActivationStatusDTO implements Serializable{

    private String title;
    private String header;
    private boolean noError;
}
