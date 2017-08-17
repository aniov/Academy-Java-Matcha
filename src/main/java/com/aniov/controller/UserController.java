package com.aniov.controller;

import com.aniov.model.Picture;
import com.aniov.model.Profile;
import com.aniov.model.SiteUserDetails;
import com.aniov.model.User;
import com.aniov.model.dto.GenericResponseDTO;
import com.aniov.model.dto.ProfileDTO;
import com.aniov.service.PictureService;
import com.aniov.service.ProfileService;
import com.aniov.service.UserService;
import com.aniov.utils.WebSocketTransmit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.MultipartConfigElement;
import javax.validation.Valid;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * User controller
 */

@RestController
public class UserController {

    @Value("${photo.how-many}")
    private int MAX_NR_OF_PHOTOS;

    @Value("${photo.size}")
    private double PHOTO_SIZE;

    private final double MEGABYTE = 1024L * 1024L;

    @Autowired
    @Qualifier("sessionRegistry")
    private SessionRegistry sessionRegistry;

    @Autowired
    private UserService userService;

    @Autowired
    private ProfileService profileService;

    @Autowired
    private PictureService pictureService;

    @Autowired
    private WebSocketTransmit webSocketTransmit;

    @GetMapping(path = "/user")
    public ResponseEntity<?> getCurrentUser() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User authUser = userService.findUserByUserName(username);

        return new ResponseEntity<>(authUser, HttpStatus.OK);
    }

    @GetMapping(path = "/user/profile", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getProfile(@RequestParam(name = "username", required = false) String username) {

        Profile profile;

        if (username == null || userService.findUserByUserName(username) == null) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String authUsername = auth.getName();
            profile = userService.findUserByUserName(authUsername).getProfile();
        } else {
            profile = userService.findUserByUserName(username).getProfile();
        }
        return new ResponseEntity<>(new ProfileDTO(profile), HttpStatus.OK);
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
        return new ResponseEntity<>(profile.getPictures(), HttpStatus.OK);

    }

    /**
     * Upload a new photo
     *
     * @param image Image data sent from front-end
     * @return saved Picture
     */
    @PostMapping(path = "/user/upload-photo")
    public ResponseEntity<?> savePhoto(@RequestParam("image") MultipartFile image) throws IOException {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String authUsername = auth.getName();

        Profile profile = profileService.findByUserName(authUsername);

        System.out.println(" size: " + (image.getSize() / MEGABYTE) + " " + " " + image.getSize());

        double imageSizeMB = image.getSize() / MEGABYTE;

        //We set a max number of pictures a user can have
        if (profile.getPictures() != null && profile.getPictures().size() >= MAX_NR_OF_PHOTOS) {
            return new ResponseEntity<>(new GenericResponseDTO("Max numbers(" + MAX_NR_OF_PHOTOS +
                    ") of photo exceeded. Please delete one photo."), HttpStatus.INSUFFICIENT_STORAGE);
        } else if (imageSizeMB > PHOTO_SIZE) {
            return new ResponseEntity<>(new GenericResponseDTO("Max image size is " + PHOTO_SIZE + "MB. Your's is " +
                    new DecimalFormat("##.##").format(imageSizeMB) + "MB"), HttpStatus.INSUFFICIENT_STORAGE);
        }
        Picture savedPicture = pictureService.savePicture(image, profile);

        return new ResponseEntity<>(savedPicture, HttpStatus.OK);
    }

    /**
     * Delete a Picture from user profile
     *
     * @param id id of Picture
     * @return HttpStatus.OK if deleted successful, HttpStatus.FORBIDDEN if picture id doesn't belong to auth user
     */
    @DeleteMapping(path = "/user/delete-photo")
    public ResponseEntity<?> deletePhoto(@RequestParam("id") Long id) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String authUsername = auth.getName();

        Profile profile = profileService.findByUserName(authUsername);

        List<Picture> authUserPictures = profile.getPictures();
        for (Picture picture : authUserPictures) {
            if (picture.getId() == id) {
                pictureService.deletePictureById(picture, profile);
                return new ResponseEntity<>(new GenericResponseDTO("Photo deleted"), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(new GenericResponseDTO("Error deleting - Forbidden"), HttpStatus.FORBIDDEN);
    }

    /**
     * Set a photo as main profile
     *
     * @param id id of photo
     * @return HttpStatus.OK
     */
    @PutMapping(path = "/user/set-main-photo")
    public ResponseEntity<?> setPhotoAsMain(@RequestParam("id") Long id) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String authUsername = auth.getName();
        Profile profile = profileService.findByUserName(authUsername);

        pictureService.setAsMainPhoto(id, profile);
        return new ResponseEntity<>(new GenericResponseDTO("Photo set as main"), HttpStatus.OK);
    }

    /**
     * Get main photo of a user
     *
     * @param username username of the requested photo
     * @return Picture
     */
    @GetMapping(path = "/user/photo-main")
    public ResponseEntity<?> getMainPhoto(@RequestParam(name = "name", required = false) String username) {

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

        Picture picture = pictureService.getMainPhoto(profile);
        return new ResponseEntity<>(picture, HttpStatus.OK);

    }

    /**
     * Add a Like to a User or un-Like him
     *
     * @param username User to give Like
     * @return HttpStatus.OK
     */
    @PostMapping(path = "/user/like")
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
    @GetMapping(path = "/user/online")
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

    /**
     * Change the default maxSize limit of the uploaded file
     */
    @Bean
    MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize("10MB");
        factory.setMaxRequestSize("10MB");
        return factory.createMultipartConfig();
    }


}
