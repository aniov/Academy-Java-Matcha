package com.aniov.service;

import com.aniov.model.Picture;
import com.aniov.model.Profile;
import com.aniov.repository.PictureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * Service service
 */
@Service
public class PictureService {

    @Autowired
    private PictureRepository pictureRepository;

    @Autowired
    private ProfileService profileService;

    /**
     * Save the Picture to data base
     *
     * @param file    picture data
     * @param profile auth user profile
     * @return saved Picture from DB
     */
    public Picture savePicture(MultipartFile file, Profile profile) {

        Picture newPicture = new Picture();
        newPicture.setDate(new Date());
        newPicture.setPictureName(file.getOriginalFilename());
        try {
            newPicture.setPictureData(file.getBytes());

            BufferedImage bufferedImage = ImageIO.read(file.getInputStream());
            newPicture.setPictureHeight(bufferedImage.getHeight());
            newPicture.setPictureWidth(bufferedImage.getWidth());
        } catch (IOException e) {
            return null;
        }

        newPicture.setProfile(profile);

        return pictureRepository.save(newPicture);
    }

    public void deletePictureById(Picture picture, Profile profile) {
        profile.getPictures().remove(picture);
        pictureRepository.delete(picture);
    }

    public void setAsMainPhoto(Long id, Profile profile) {

        List<Picture> pictures = profile.getPictures();
        for (Picture picture : pictures) {
            if (id == picture.getId()) {
                picture.setProfilePicture(true);
            } else {
                picture.setProfilePicture(false);
            }
            pictureRepository.save(picture);
        }
    }

    public Picture getMainPhoto(Profile profile) {

        List<Picture> pictures = profile.getPictures();
        for (Picture picture : pictures) {
            if (picture.isProfilePicture()) {
                return picture;
            }
        }
        return null;
    }
}
