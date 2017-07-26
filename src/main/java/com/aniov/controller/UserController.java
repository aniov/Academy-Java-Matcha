package com.aniov.controller;

import com.aniov.model.Profile;
import com.aniov.model.User;
import com.aniov.model.dto.GenericResponseDTO;
import com.aniov.service.ProfileService;
import com.aniov.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private ProfileService profileService;


    @GetMapping(path = "/user/me")
    public ResponseEntity<?> getCurrentUser() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User currentUser =  userService.findUserByUserName(username);

        return new ResponseEntity<>(currentUser, HttpStatus.OK);
    }

    @GetMapping(path = "/user/profile")
    public ResponseEntity<?> getProfile(@RequestParam(name = "name", required = false) String username) {

        Profile profile;

        if (username == null) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String retrieveUser = auth.getName();
            profile = userService.findUserByUserName(retrieveUser).getProfile();
            System.out.println("user name: " + retrieveUser + " " + profile);
        } else {
            if (userService.findUserByUserName(username) == null) {
                return new ResponseEntity<>(new GenericResponseDTO("User not found"), HttpStatus.NOT_FOUND);
            }
            profile = userService.findUserByUserName(username).getProfile();
        }

        return new ResponseEntity<>(profile, HttpStatus.OK);
    }
}
