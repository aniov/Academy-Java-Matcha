package com.aniov.controller;

import com.aniov.model.Interest;
import com.aniov.service.ProfileService;
import com.aniov.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

/**
 * A Interest(Tag) Controller
 */
@RestController
public class InterestController {

    @Autowired
    private ProfileService profileService;

    @Autowired
    private UserService userService;

    /**
     * Find all user Interest
     *
     * @param username username of User to be searched
     * @return Set<Interest>
     */
    @GetMapping(path = "/user/interest")
    public ResponseEntity<?> getAllInterest(@RequestParam(name = "username", required = false) String username) {

        if (username == null || userService.findUserByUserName(username) == null) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            username = auth.getName();
        }

        Set<Interest> interests = profileService.getAllInterest(username);
        return new ResponseEntity<>(interests, HttpStatus.OK);
    }

    /**
     * Save a new Interest to user profile
     *
     * @param interest interest to be saved
     * @return HttpStatus.OK
     */
    @PostMapping(path = "/user/interest")
    public ResponseEntity<?> saveInterest(@RequestParam(name = "i") String interest) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String authUsername = auth.getName();

        profileService.addInterest(interest.toLowerCase(), authUsername);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Delete a user Interest
     *
     * @param interest interest to be deleted
     * @return HttpStatus.OK
     */
    @DeleteMapping(path = "/user/interest")
    public ResponseEntity<?> deleteInterest(@RequestParam(name = "i") String interest) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String authUsername = auth.getName();

        profileService.deleteInterest(interest.toLowerCase(), authUsername);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
