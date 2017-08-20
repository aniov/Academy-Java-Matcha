package com.aniov.controller;

import com.aniov.model.dto.ProfileDTO;
import com.aniov.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for Profiles
 */
@RestController
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    /**
     * Finds profiles by our match algorithm
     *
     * @return List<ProfileDTO>
     */
    @GetMapping(path = "/profiles")
    public ResponseEntity<?> getMatchingProfiles(@RequestParam(name = "page") int page) {

        Page<ProfileDTO> profileDTOS = profileService.getMatchingProfiles(page);
        return new ResponseEntity<>(profileDTOS, HttpStatus.OK);
    }

    /**
     * Finds Profiles containing letters like
     *
     * @param namelike     string letters to be searched after
     * @param locationlike string letters to be searched after
     * @param interest     string to be searched after
     * @return List<ProfileDTO>
     */
    @GetMapping(path = "profile/search")
    public ResponseEntity<?> getProfilesBy(@RequestParam(name = "name-like", required = false) String namelike,
                                           @RequestParam(name = "location-like", required = false) String locationlike,
                                           @RequestParam(name = "interest", required = false) String interest,
                                           @RequestParam(name = "page") int page) {

        Page<ProfileDTO> profileDTOS;

        if (namelike != null) {
            profileDTOS = profileService.finByNameContaining(namelike, page);
        } else if (locationlike != null) {
            profileDTOS = profileService.findProfilesByLocation(locationlike, page);
        } else if (interest != null) {
            profileDTOS = profileService.findProfilesByInterest(interest, page);
        } else {
            profileDTOS = profileService.getMatchingProfiles(page);
        }
        return new ResponseEntity<>(profileDTOS, HttpStatus.OK);

    }
}
