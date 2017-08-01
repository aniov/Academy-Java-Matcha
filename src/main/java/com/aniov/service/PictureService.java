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

/**
 * Service service
 */
@Service
public class PictureService {

    @Autowired
    private PictureRepository pictureRepository;

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
        newPicture.setProfilePicture(true);
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
}
