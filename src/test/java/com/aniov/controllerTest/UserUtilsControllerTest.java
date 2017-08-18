package com.aniov.controllerTest;

import com.aniov.model.dto.UserRegisterDTO;
import com.aniov.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test class for user utilities controller
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class UserUtilsControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private UserService userService;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
    }

    @Test
    public void registerNewUser_successTest() throws Exception {

        UserRegisterDTO registerDTO = new UserRegisterDTO("Johny", "john@john.com", "password", "password");

        mockMvc.perform(
                post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(registerDTO))
                        .with(csrf()))
                .andExpect(status().isCreated());
    }

    @Test
    public void registerNewUser_errorTest() throws Exception {

        List<UserRegisterDTO> registerDTOS = Arrays.asList(
                new UserRegisterDTO("Joh", "john@john.com", "password", "password"),
                new UserRegisterDTO("Johny", "john-john.com", "password", "password"),
                new UserRegisterDTO("Johny", "john@john.com", "Password", "password"));

        for (UserRegisterDTO userRegisterDTO : registerDTOS) {
            mockMvc.perform(
                    post("/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(userRegisterDTO))
                            .with(csrf()))
                    .andExpect(status().isNotAcceptable());
        }

        for (int i = 0; i < 40; i++) {

            UserRegisterDTO userRegisterDTO = new UserRegisterDTO("User Name " + i, "email@email.com" + i, "password", "password");
            userService.registerNewUser(userRegisterDTO);
        }
    }

    /**
     * Converts a Java object into JSON representation
     */
    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
