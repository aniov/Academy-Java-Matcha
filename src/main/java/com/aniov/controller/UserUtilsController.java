package com.aniov.controller;

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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * User utils controller
 */

@RestController
public class UserUtilsController {

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private VerificationTokenService verificationTokenService;

    /**
     * Register a new user
     *
     * @param userRegisterDTO a dto for new user
     * @param result          BindingResult
     * @return HttpStatus.CREATED
     * @throws MessagingException
     */
    @PostMapping(path = "/register", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> registerNewUser(@RequestBody @Valid UserRegisterDTO userRegisterDTO, BindingResult result) throws MessagingException {

        List<String> errors = getErrorMessage(result);

        if (!errors.isEmpty()) {
            return new ResponseEntity<>(new GenericResponseDTO(errors), HttpStatus.NOT_ACCEPTABLE);
        }
        User savedUser = userService.registerNewUser(userRegisterDTO);

        if (savedUser != null) {
            emailService.sendRegistrationToken(savedUser);
            return new ResponseEntity<>(new GenericResponseDTO("Account created"), HttpStatus.CREATED);
        }
        return new ResponseEntity<>(new GenericResponseDTO("Account could NOT be created"), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Checks email and creates a reset password token for that user, sends a email to that email address
     *
     * @param resetPasswordDTO a dto for user email
     * @param result           BindingResult
     * @return HttpStatus.ACCEPTED
     * @throws IOException
     */
    @PostMapping(path = "/resetpassword", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> resetPassword(@RequestBody @Valid ResetPasswordDTO resetPasswordDTO, BindingResult result) throws IOException {

        List<String> errors = getErrorMessage(result);

        if (!errors.isEmpty()) {
            return new ResponseEntity<>(new GenericResponseDTO(errors), HttpStatus.FORBIDDEN);
        }
        String email = resetPasswordDTO.getEmail();
        User user = userService.findUserByEmail(email);
        if (user == null) {
            return new ResponseEntity<>(new GenericResponseDTO("Check your email again"), HttpStatus.FORBIDDEN);
        }

        if (!user.getAccount().isEnabled()) {
            return new ResponseEntity<>(new GenericResponseDTO("Activate your account first"), HttpStatus.FORBIDDEN);
        }

        try {
            emailService.resetPasswordToken(user);
        } catch (MessagingException e) {
            return new ResponseEntity<>(new GenericResponseDTO("Please try again later"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(new GenericResponseDTO("An email has been sent"), HttpStatus.ACCEPTED);
    }

    /**
     * Changes the password if the received token is valid
     *
     * @param tokenString       token from email linked to the user account
     * @param changePasswordDTO a dto for password change
     * @param result            BindingResult
     * @return HttpStatus.OK
     */
    @PostMapping(path = "/changepassword", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> changePassword(@RequestParam(name = "token") String tokenString,
                                            @RequestBody @Valid ChangePasswordDTO changePasswordDTO, BindingResult result) {

        VerificationToken token = verificationTokenService.getVerificationToken(tokenString);

        if (token == null) {
            return new ResponseEntity<>(new GenericResponseDTO("Token not found"), HttpStatus.FORBIDDEN);
        }
        Date expiryDate = token.getExpiryDate();
        if (expiryDate.before(new Date()) || !token.getTokenType().equals(TokenType.PASSWORD_RESET)) {
            verificationTokenService.deleteToken(token);
            return new ResponseEntity<>(new GenericResponseDTO("Token expired"), HttpStatus.FORBIDDEN);
        }
        User user = token.getUser();
        if (user == null) {
            verificationTokenService.deleteToken(token);
            return new ResponseEntity<>(new GenericResponseDTO("User not found"), HttpStatus.FORBIDDEN);
        }
        List<String> errors = getErrorMessage(result);

        if (!errors.isEmpty()) {
            return new ResponseEntity<>(new GenericResponseDTO(errors), HttpStatus.FORBIDDEN);
        }
        userService.changeUserPassword(user, changePasswordDTO.getPlainPassword());
        verificationTokenService.deleteToken(token);

        return new ResponseEntity<>(new GenericResponseDTO("Password changed"), HttpStatus.OK);
    }

    /**
     * Customize error message for register page
     */
    private List<String> getErrorMessage(BindingResult result) {

        List<String> messages = new ArrayList<>();

        for (ObjectError object : result.getAllErrors()) {
            messages.add(object.getDefaultMessage());
        }
        return messages;
    }


}
