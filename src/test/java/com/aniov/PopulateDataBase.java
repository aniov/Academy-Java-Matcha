package com.aniov;

import com.aniov.model.Picture;
import com.aniov.model.Profile;
import com.aniov.model.User;
import com.aniov.model.dto.UserRegisterDTO;
import com.aniov.repository.PictureRepository;
import com.aniov.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.imageio.ImageIO;
import javax.transaction.Transactional;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class PopulateDataBase {

    private static int HOW_MANY_USERS = 50;

    @Autowired
    private UserService userService;

    @Autowired
    private PictureRepository pictureRepository;

    @Test
    public void createMockUsers() {

        String ex = ".jpg";

        for (int i = 22; i < HOW_MANY_USERS; i++) {
            UserRegisterDTO userRegisterDTO = new UserRegisterDTO("Mockita" + i, "aemail@email.com" + i, "password", "password");
            userService.registerNewUser(userRegisterDTO);
            BufferedImage image = null;
            try {
                image = ImageIO.read(new File("C:\\Users\\aniov\\Desktop\\Profile-photos\\Man\\" + (i + 1) + ex));

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(image, "jpg", baos);
                baos.flush();
                byte[] imageInByte = baos.toByteArray();
                baos.close();

                User user = userService.findUserByUserName("Mockito" + i);

                Profile profile = user.getProfile();

                Picture picture = new Picture();
                picture.setProfilePicture(true);
                picture.setProfile(profile);
                picture.setDate(new Date());
                picture.setPictureName("mockito pic");
                picture.setPictureData(imageInByte);
                picture.setPictureHeight(image.getHeight());
                picture.setPictureWidth(image.getWidth());

                pictureRepository.saveAndFlush(picture);

            } catch (IOException e) {
                e.printStackTrace();
                if (ex.equals(".jpg")) {
                    ex = ".jpeg";
                    i--;
                } else {
                    ex = ".jpg";
                    i--;
                }
            }
        }
    }
}
