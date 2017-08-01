package com.aniov.controller;

import com.aniov.model.Profile;
import com.aniov.model.User;
import com.aniov.model.dto.GenericResponseDTO;
import com.aniov.model.dto.ProfileDTO;
import com.aniov.service.ProfileService;
import com.aniov.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

/**
 * User controller
 */

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private ProfileService profileService;

    @GetMapping(path = "/user")
    public ResponseEntity<?> getCurrentUser() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User authUser = userService.findUserByUserName(username);

        return new ResponseEntity<>(authUser, HttpStatus.OK);
    }

    @GetMapping(path = "/user/profile", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getProfile(@RequestParam(name = "name", required = false) String username) {

        Profile profile;

        if (username == null) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String authUsername = auth.getName();
            profile = userService.findUserByUserName(authUsername).getProfile();
        } else {
            if (userService.findUserByUserName(username) == null) {
                return new ResponseEntity<>(new GenericResponseDTO("User not found"), HttpStatus.NOT_FOUND);
            }
            profile = userService.findUserByUserName(username).getProfile();
        }
        return new ResponseEntity<Object>(new ProfileDTO(profile), HttpStatus.OK);
    }

    @PostMapping(path = "/user/profile", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> changeProfile(@RequestBody @Valid ProfileDTO profileDTO, BindingResult result) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String authUsername = auth.getName();

        Profile savedProfile = profileService.saveProfile(profileDTO, authUsername);


        return new ResponseEntity<>(new ProfileDTO(savedProfile), HttpStatus.OK);
    }

    @GetMapping(path = "/user/photos")
    public ResponseEntity<?> getPhotos(@RequestParam(name = "name", required = false) String username) {

        Profile profile;

        if (username == null) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String authUsername = auth.getName();
            profile = userService.findUserByUserName(authUsername).getProfile();
        } else {
            if (userService.findUserByUserName(username) == null) {
                return new ResponseEntity<>(new GenericResponseDTO("User not found."), HttpStatus.NOT_FOUND);
            }
            profile = userService.findUserByUserName(username).getProfile();
        }
        return new ResponseEntity<Object>(profile.getPictures(), HttpStatus.OK);

    }

    @PostMapping(path = "/user/upload-photo")
    public ResponseEntity<?> savePhoto(@RequestParam("image") MultipartFile image) {

        System.out.println("THIS iS THE PHOTO: " + image.getSize() + " " + image.getName() + " " + image.getContentType());


        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String authUsername = auth.getName();

        Profile profile = profileService.findByUserName(authUsername);

        return new ResponseEntity<>(HttpStatus.OK);
    }


}
