package com.aniov.controller;

import com.aniov.model.Interest;
import com.aniov.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/**
 * A Tag, Interest Controller
 */
@RestController
public class InterestController {

    @Autowired
    private ProfileService profileService;

    @GetMapping(path = "/user/interest")
    public ResponseEntity<?> getAllInterest(@PathVariable(name = "username") String username) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String authUsername = auth.getName();

        profileService.getAllInterest(authUsername);

        return new ResponseEntity<>(HttpStatus.OK);

    }

    @PostMapping(path = "/user/interest")
    public ResponseEntity<?> saveInterest(@RequestParam(name = "i") String interest) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String authUsername = auth.getName();

        profileService.addInterest(interest, authUsername);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
