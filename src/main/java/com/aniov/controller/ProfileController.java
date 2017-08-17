package com.aniov.controller;

import com.aniov.model.dto.ProfileDTO;
import com.aniov.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller for Profiles
 */
@RestController
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    /**
     * Finds profiles by our match algorithm or by interest || by location
     *
     * @param interest interest to be searched after
     * @return List<ProfileDTO>
     */
    @GetMapping(path = "/profiles")
    public ResponseEntity<?> getMatchingProfiles(@RequestParam(name = "interest", required = false) String interest) {

        List<ProfileDTO> profileDTOS;

        if (interest != null) {
            profileDTOS = profileService.findProfilesByInterest(interest);
        } else {
            profileDTOS = profileService.getMatchingProfiles();
        }
        return new ResponseEntity<>(profileDTOS, HttpStatus.OK);
    }

    /**
     * Finds Profiles containing letters like
     *
     * @param namelike     string letters to be searched after
     * @param locationlike string letters to be searched after
     * @return List<ProfileDTO>
     */
    @GetMapping(path = "profile/search")
    public ResponseEntity<?> getProfilesByUsername(@RequestParam(name = "name-like", required = false) String namelike,
                                                   @RequestParam(name = "location-like", required = false) String locationlike) {

        List<ProfileDTO> profileDTOS;

        if (namelike != null) {
            profileDTOS = profileService.finByNameContaining(namelike);
        } else if (locationlike != null) {
            profileDTOS = profileService.findProfilesByLocation(locationlike);
        } else {
            profileDTOS = profileService.getMatchingProfiles();
        }
        return new ResponseEntity<>(profileDTOS, HttpStatus.OK);

    }
}
