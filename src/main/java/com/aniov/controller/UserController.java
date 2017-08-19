package com.aniov.controller;

import com.aniov.model.Profile;
import com.aniov.model.SiteUserDetails;
import com.aniov.model.User;
import com.aniov.model.dto.GenericResponseDTO;
import com.aniov.model.dto.ProfileDTO;
import com.aniov.service.ProfileService;
import com.aniov.service.UserService;
import com.aniov.service.VisitorService;
import com.aniov.utils.WebSocketTransmit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * User controller
 */
@RestController
@RequestMapping(path = "/user")
public class UserController {

    @Autowired
    @Qualifier("sessionRegistry")
    private SessionRegistry sessionRegistry;

    @Autowired
    private UserService userService;

    @Autowired
    private ProfileService profileService;

    @Autowired
    private WebSocketTransmit webSocketTransmit;

    @Autowired
    private VisitorService visitorService;

    @GetMapping
    public ResponseEntity<?> getCurrentUser() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User authUser = userService.findUserByUserName(username);

        return new ResponseEntity<>(authUser, HttpStatus.OK);
    }

    @GetMapping(path = "/profile", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getProfile(@RequestParam(name = "username", required = false) String username) {

        Profile profile;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (username == null || userService.findUserByUserName(username) == null) {
            String authUsername = auth.getName();
            profile = profileService.findByUserName(authUsername);
        } else {
            profile = profileService.findByUserName(username);
            if (!username.equals(auth.getName())) {
                visitorService.addNewVisit(auth.getName(), username);
            }

        }
        return new ResponseEntity<>(new ProfileDTO(profile), HttpStatus.OK);
    }

    @PostMapping(path = "/profile", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> changeProfile(@RequestBody @Valid ProfileDTO profileDTO, BindingResult result) {

        if (result.hasErrors()) {
            return new ResponseEntity<>(new GenericResponseDTO("Input fields have errors"), HttpStatus.BAD_REQUEST);
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String authUsername = auth.getName();

        Profile savedProfile = profileService.saveProfile(profileDTO, authUsername);
        return new ResponseEntity<>(new ProfileDTO(savedProfile), HttpStatus.OK);
    }

    /**
     * Add a Like to a User or un-Like him
     *
     * @param username User to give Like
     * @return HttpStatus.OK
     */
    @PostMapping(path = "/like")
    public ResponseEntity<?> addLikeToAnotherUser(@RequestParam(name = "name") String username) {

        if (username == null || userService.findUserByUserName(username) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String authUsername = auth.getName();
        if (username.equals(authUsername)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        String likeAdded = "false";
        Profile authUserProfile = userService.findUserByUserName(authUsername).getProfile();
        User userToGiveLike = userService.findUserByUserName(username);

        if (authUserProfile.getLikesGiven().contains(userToGiveLike.getProfile())) {
            authUserProfile.removeLike(userToGiveLike.getProfile());
            webSocketTransmit.sendLikeOverSocket(authUsername, username, false);
        } else {
            authUserProfile.addLikeToUser(userToGiveLike.getProfile());
            likeAdded = "true";
            webSocketTransmit.sendLikeOverSocket(authUsername, username, true);
        }

        profileService.saveProfileEntity(authUserProfile);
        return new ResponseEntity<>(new GenericResponseDTO(likeAdded), HttpStatus.OK);
    }

    /**
     * Search all online users
     *
     * @return online users
     */
    @GetMapping(path = "/online")
    public ResponseEntity<?> getLoggedUsers() {

        List<Object> principals = sessionRegistry.getAllPrincipals();
        Set<String> loggedUsers = new LinkedHashSet<>();

        for (Object principal : principals) {
            if (principal instanceof SiteUserDetails) {
                loggedUsers.add(((SiteUserDetails) principal).getUsername());
            }
        }
        return new ResponseEntity<>(loggedUsers, HttpStatus.OK);
    }

}
