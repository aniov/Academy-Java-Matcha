package com.aniov.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * User registration controller
 */

@RestController
public class Register {

    @PostMapping(value = "/register")
    public ResponseEntity<?> registerNewUser() {
        return new ResponseEntity(HttpStatus.CREATED);
    }
}
