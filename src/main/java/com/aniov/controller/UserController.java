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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
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

    @PostMapping(path = "/resetpassword", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> resetPassword(@RequestBody @Valid ResetPasswordDTO resetPasswordDTO, BindingResult result) throws IOException {

        if (result.hasErrors()) {
            return new ResponseEntity<>(new GenericResponseDTO("Enter a valid email", "Email field error"), HttpStatus.FORBIDDEN);
        }
        String email = resetPasswordDTO.getEmail();
        User user = userService.findUserByEmail(email);
        if (user == null) {
            return new ResponseEntity<>(new GenericResponseDTO("Check your email again", "Email not found"), HttpStatus.FORBIDDEN);
        }

        if (!user.getAccount().isEnabled()) {
            return new ResponseEntity<>(new GenericResponseDTO("Activate your account first", "Account is not activated"), HttpStatus.FORBIDDEN);
        }

        try {
            emailService.resetPasswordToken(user);
        } catch (MessagingException e) {
            return new ResponseEntity<>(new GenericResponseDTO("Please try again later", "Server error"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(new GenericResponseDTO("An email has been sent", "password changed"), HttpStatus.ACCEPTED);
    }

    @PostMapping(path = "/changepassword", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> changePassword(@RequestParam(name = "token") String tokenString,
                                            @RequestBody @Valid ChangePasswordDTO changePasswordDTO, BindingResult result) {

        VerificationToken token = verificationTokenService.getVerificationToken(tokenString);

        if (token == null) {
            return new ResponseEntity<>(new GenericResponseDTO("Token not found", "error: token not in db"), HttpStatus.FORBIDDEN);
        }

        Date expiryDate = token.getExpiryDate();
        if (expiryDate.before(new Date()) || !token.getTokenType().equals(TokenType.PASSWORD_RESET)) {
            verificationTokenService.deleteToken(token);
            return new ResponseEntity<>(new GenericResponseDTO("Token expired", "error: token is expired"), HttpStatus.FORBIDDEN);
        }

        User user = token.getUser();
        if (user == null) {
            verificationTokenService.deleteToken(token);
            return new ResponseEntity<>(new GenericResponseDTO("User not found", "error: user not found"), HttpStatus.FORBIDDEN);
        }

        if (result.hasErrors()) {
            return new ResponseEntity<>(new GenericResponseDTO("Enter a valid password", "error: invalid input"), HttpStatus.FORBIDDEN);
        }

        userService.changeUserPassword(user, changePasswordDTO.getPlainPassword());

        verificationTokenService.deleteToken(token);

        return new ResponseEntity<>(new GenericResponseDTO("Password changed", "password has been changed"), HttpStatus.OK);
    }


}
