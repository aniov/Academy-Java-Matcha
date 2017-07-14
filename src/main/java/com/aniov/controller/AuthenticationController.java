package com.aniov.controller;

import com.aniov.jwtConfig.JwtTokenUtil;
import com.aniov.model.dto.JwtLoginDTO;
import com.aniov.model.dto.JwtLoginResponseDTO;
import com.aniov.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * This is the login Class Controller
 * Here we generate a token in exchange of valid username & password
 */

@RestController
public class AuthenticationController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    /**
     * Login method
     *
     * @param jwtLoginDTO login form containing two fields: username and password
     * @param device      device type
     * @param result      possible errors in jwtLoginDTO
     * @return a valid JWT if the JwtLoginDTO is valid and found in DB
     */
    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> loginAndCreateToken(@RequestBody @Valid JwtLoginDTO jwtLoginDTO,
                                                 Device device, BindingResult result) {

        if (result.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                jwtLoginDTO.getUsername(), jwtLoginDTO.getPlainPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetails userDetails = userService.loadUserByUsername(jwtLoginDTO.getUsername());

        String token = jwtTokenUtil.generateJwtToken(userDetails, device);

        return new ResponseEntity<>(new JwtLoginResponseDTO(token), HttpStatus.OK);
    }
}
