package com.aniov.controller;

import com.aniov.model.TokenType;
import com.aniov.model.User;
import com.aniov.model.VerificationToken;
import com.aniov.service.UserService;
import com.aniov.service.VerificationTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.stream.Collectors;

/**
 * Navigation between pages
 */

@Controller
public class NavigationController {

    @Autowired
    private VerificationTokenService verificationTokenService;

    @Autowired
    private UserService userService;

    @Autowired
    private SessionRegistry sessionRegistry;

    @GetMapping(path = "/")
    public String home() {
        return "redirect:/main";
    }

    @GetMapping(path = "/register")
    public String register() {
        return "registerpage.html";
    }

    @GetMapping(path = "/resetpassword")
    public String resetPassword() {
        return "resetpasswordpage.html";
    }

    @GetMapping(path = "/changepassword")
    public String changePassword() {
        return "changepasswordpage.html";
    }

    @GetMapping(path = "/main")
    public String mainPage() {
        System.out.println("Redirect to main paige");
        return "usermainpage.html";
    }

    @GetMapping(path = "/activate")
    public String activateAccount(@RequestParam(name = "token") String tokenString, HttpServletResponse response) throws IOException {

        VerificationToken token = verificationTokenService.getVerificationToken(tokenString);

        if (token == null) {
            // return new ResponseEntity<>(new GenericResponseDTO("Token expired", "this is the error"), HttpStatus.NOT_FOUND);
        }
        Date expiryDate = token.getExpiryDate();

        if (expiryDate.before(new Date()) || !token.getTokenType().equals(TokenType.ACTIVATION)) {
            verificationTokenService.deleteToken(token);
            // return new ResponseEntity<>(new GenericResponseDTO("Token not valid", "this is the error"), HttpStatus.NOT_ACCEPTABLE);
        }

        User user = token.getUser();
        if (user == null) {
            verificationTokenService.deleteToken(token);
            // return new ResponseEntity<>(new GenericResponseDTO("User not found", "this is the error"), HttpStatus.NOT_ACCEPTABLE);
        }

        user.getAccount().setEnabled(true);
        userService.saveUser(user);
        verificationTokenService.deleteToken(token);
        return "accountactivatedpage.html";
    }
}
