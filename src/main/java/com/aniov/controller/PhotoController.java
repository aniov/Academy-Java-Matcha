package com.aniov.controller;

import com.aniov.model.Picture;
import com.aniov.model.Profile;
import com.aniov.model.dto.GenericResponseDTO;
import com.aniov.service.PictureService;
import com.aniov.service.ProfileService;
import com.aniov.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.MultipartConfigElement;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;

@RestController
public class PhotoController {

    @Value("${photo.how-many}")
    private int MAX_NR_OF_PHOTOS;

    @Value("${photo.size}")
    private double PHOTO_SIZE;

    private final double MEGABYTE = 1024L * 1024L;

    @Autowired
    private PictureService pictureService;

    @Autowired
    private UserService userService;

    @Autowired
    private ProfileService profileService;

    @GetMapping(path = "/user/photos")
    public ResponseEntity<?> getPhotos(@RequestParam(name = "name", required = false) String username) {

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
                return new ResponseEntity<>(new GenericResponseDTO("Username not found"), HttpStatus.NOT_FOUND);
            }
            profile = userService.findUserByUserName(username).getProfile();
        }

        Picture picture = pictureService.getMainPhoto(profile);
        return new ResponseEntity<>(picture, HttpStatus.OK);

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
