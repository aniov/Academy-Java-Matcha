package com.aniov.controller;

import com.aniov.jwtConfig.JwtTokenUtil;
import com.aniov.model.TokenType;
import com.aniov.model.User;
import com.aniov.model.VerificationToken;
import com.aniov.model.dto.ResetPasswordDTO;
import com.aniov.model.dto.UserRegisterDTO;
import com.aniov.service.UserService;
import com.aniov.service.VerificationTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Date;

/**
 * User registration controller
 */

@RestController
public class Register {

    @Value("${config.security.header}")
    private String tokenHeader;

    @Autowired
    private UserService userService;

    @Autowired
    private VerificationTokenService verificationTokenService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @PostMapping(path = "/register", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> registerNewUser(@RequestBody @Valid UserRegisterDTO userRegisterDTO, BindingResult result) {

        if (result.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (!userService.registerNewUser(userRegisterDTO)) {
            return new ResponseEntity(HttpStatus.CREATED);
        }

        return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping(path = "/activate")
    public ResponseEntity<?> activateAccount(@RequestParam(name = "token") String tokenString) {

        VerificationToken token = verificationTokenService.getVerificationToken(tokenString);

        if (token == null) {
            return new ResponseEntity<>("NULL TOKEN", HttpStatus.NOT_FOUND);
        }
        Date expiryDate = token.getExpiryDate();

        if (expiryDate.before(new Date()) || !token.getTokenType().equals(TokenType.ACTIVATION)) {
            verificationTokenService.deleteToken(token);
            return new ResponseEntity<>("EXPIRED", HttpStatus.NOT_ACCEPTABLE);
        }

        User user = token.getUser();
        if (user == null) {
            verificationTokenService.deleteToken(token);
            return new ResponseEntity<>("NO SUCH USER", HttpStatus.NOT_ACCEPTABLE);
        }

        user.getAccount().setEnabled(true);
        userService.saveUser(user);
        verificationTokenService.deleteToken(token);
        return new ResponseEntity<>("ALL OK-ACTIVATED", HttpStatus.OK);
    }

    //TODO !!!!!!!! produce token and send email, redirect
    @PostMapping(path = "/resetpassword", consumes = MediaType.APPLICATION_JSON_VALUE)
    public String resetPassword(@RequestBody @Valid ResetPasswordDTO resetPasswordDTO, BindingResult result) throws IOException {

        if (result.hasErrors()) {
            return "Not ok";
        }
        String email = resetPasswordDTO.getEmail();
        User user = userService.findUserByEmail(email);
        if (user == null) {
            return "email doesn't exists";
        }

        user.setHashedPassword("empty");
        userService.saveUser(user);

        return "OK";
    }

    @PostMapping(path = "/changepassword", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> changePassword(@RequestParam(name = "token") String tokenString) {

        VerificationToken token = verificationTokenService.getVerificationToken(tokenString);

        if (token == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Date expiryDate = token.getExpiryDate();
        if (expiryDate.before(new Date()) || !token.getTokenType().equals(TokenType.PASSWORD_RESET)) {
            verificationTokenService.deleteToken(token);
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }

        User user = token.getUser();
        if (user == null) {
            verificationTokenService.deleteToken(token);
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }//TODO

        return new ResponseEntity<>(HttpStatus.OK);
    }


}
