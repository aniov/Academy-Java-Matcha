package com.aniov.controller;

import com.aniov.jwtConfig.JwtTokenUtil;
import com.aniov.model.TokenType;
import com.aniov.model.User;
import com.aniov.model.VerificationToken;
import com.aniov.model.dto.ChangePasswordDTO;
import com.aniov.model.dto.GenericResponseDTO;
import com.aniov.model.dto.ResetPasswordDTO;
import com.aniov.model.dto.UserRegisterDTO;
import com.aniov.service.EmailService;
import com.aniov.service.UserService;
import com.aniov.service.VerificationTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Date;

/**
 * User registration controller
 */

@RestController
public class UserController {

    @Value("${config.security.header}")
    private String tokenHeader;

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private VerificationTokenService verificationTokenService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @PostMapping(path = "/register", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> registerNewUser(@RequestBody @Valid UserRegisterDTO userRegisterDTO, BindingResult result) throws MessagingException {

        if (result.hasErrors()) {
            return new ResponseEntity<>(new GenericResponseDTO("ERRR", "ERRR"), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        User savedUser = userService.registerNewUser(userRegisterDTO);

        if (savedUser != null) {
            emailService.sendRegistrationToken(savedUser);
            return new ResponseEntity<>(new GenericResponseDTO("This the message", "this is the error"), HttpStatus.CREATED);
        }

        return new ResponseEntity<>(new GenericResponseDTO("This the message", "this is the error"), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping(path = "/resetpassword", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public String resetPassword(@RequestBody @Valid ResetPasswordDTO resetPasswordDTO, BindingResult result) throws IOException {

        if (result.hasErrors()) {
            return "Not ok";
        }
        String email = resetPasswordDTO.getEmail();
        User user = userService.findUserByEmail(email);
        if (user == null) {
            return "email doesn't exists";
        }

        if (! user.getAccount().isEnabled()) {
            return "Activate your account first";
        }

        try {
            emailService.resetPasswordToken(user);
        } catch (MessagingException e) {
            System.out.println("ERROR: " + e);
            e.printStackTrace();
        }
        return "OK";
    }

    @PostMapping(path = "/changepassword", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> changePassword(@RequestParam(name = "token") String tokenString,
                                            @RequestBody @Valid ChangePasswordDTO changePasswordDTO, BindingResult result) {

        System.out.println("ENTER");

        VerificationToken token = verificationTokenService.getVerificationToken(tokenString);

        System.out.println("TOKEN: " + token.getToken());

        if (token == null) {
            return new ResponseEntity<>("Token Not found", HttpStatus.BAD_REQUEST);
        }

        Date expiryDate = token.getExpiryDate();
        if (expiryDate.before(new Date()) || !token.getTokenType().equals(TokenType.PASSWORD_RESET)) {
            verificationTokenService.deleteToken(token);
            return new ResponseEntity<>("Token expired", HttpStatus.BAD_REQUEST);
        }

        User user = token.getUser();
        if (user == null) {
            verificationTokenService.deleteToken(token);
            return new ResponseEntity<>("User not found", HttpStatus.BAD_REQUEST);
        }

        if (result.hasErrors()){
            System.out.println("ERROR");
            return new ResponseEntity<>("password not ok", HttpStatus.BAD_REQUEST);
        }

        userService.changeUserPassword(user, changePasswordDTO.getPlainPassword());

        verificationTokenService.deleteToken(token);

        return new ResponseEntity<>("Password changed", HttpStatus.OK);
    }


}
