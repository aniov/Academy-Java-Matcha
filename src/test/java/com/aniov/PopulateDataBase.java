package com.aniov;

import com.aniov.model.User;
import com.aniov.model.dto.UserRegisterDTO;
import com.aniov.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class PopulateDataBase {

    private static int HOW_MANY_USERS = 50;

    @Autowired
    private UserService userService;

    @Test
    public void createMockUsers() {

        for (int i = 0; i < HOW_MANY_USERS; i++) {

            UserRegisterDTO userRegisterDTO = new UserRegisterDTO("Mockito" + i, "email@email.com" + i, "password", "password");
            userService.registerNewUser(userRegisterDTO);
          //  User user = userService.findUserByUserName("Mockito" + i);
          //  userService.deleteUser(user);
        }
    }
}
