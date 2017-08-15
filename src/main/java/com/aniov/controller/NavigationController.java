package com.aniov.controller;

import com.aniov.model.TokenType;
import com.aniov.model.User;
import com.aniov.model.VerificationToken;
import com.aniov.model.dto.ActivationStatusDTO;
import com.aniov.service.UserService;
import com.aniov.service.VerificationTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.Date;

/**
 * Navigation between pages
 */

@Controller
public class NavigationController {

    @Autowired
    private VerificationTokenService verificationTokenService;

    @Autowired
    private UserService userService;

    @GetMapping(path = "/")
    public String home() {
        return "redirect:/main";
    }

    @GetMapping(path = "/login")
    public String login() {
        return "loginpage";
    }

    @GetMapping(path = "/register")
    public String register() {
        return "registerpage";
    }

    @GetMapping(path = "/resetpassword")
    public String resetPassword() {
        return "resetpasswordpage";
    }

    @GetMapping(path = "/changepassword")
    public String changePassword() {
        return "changepasswordpage";
    }

    @GetMapping(path = "/main")
    public String main() {
        return "usermainpage";
    }

    @GetMapping(path = "/profile")
    public String profile() {
        return "profilepage";
    }

    @GetMapping(path = "/photo")
    public String photos() {
        return "photopage";
    }

    @GetMapping(path = "/message")
    public String messages() {
        return "messagepage";
    }

    @GetMapping(path = "/activate")
    public String activateAccount(@RequestParam(name = "token") String tokenString, Model model) throws IOException {

        VerificationToken token = verificationTokenService.getVerificationToken(tokenString);
        ActivationStatusDTO response = new ActivationStatusDTO();
        model.addAttribute("response", response);

        if (token == null) {
            response.setHeader("Error");
            response.setTitle("TOKEN WAS NOT FOUND");
            response.setNoError(false);
            return "accountactivatedpage";
        }
        Date expiryDate = token.getExpiryDate();

        if (expiryDate.before(new Date()) || !token.getTokenType().equals(TokenType.ACTIVATION)) {
            verificationTokenService.deleteToken(token);
            response.setHeader("Error");
            response.setTitle("TOKEN IS EXPIRED");
            response.setNoError(false);
            return "accountactivatedpage";
        }

        User user = token.getUser();
        if (user == null) {
            verificationTokenService.deleteToken(token);
            response.setHeader("Error");
            response.setTitle("USER DOESN'T EXISTS");
            response.setNoError(false);
            return "accountactivatedpage";
        }

        user.getAccount().setEnabled(true);
        userService.saveUser(user);
        verificationTokenService.deleteToken(token);
        response.setHeader("Activated");
        response.setTitle("YOUR ACCOUNT HAS BEEN ACTIVATED");
        response.setNoError(true);
        return "accountactivatedpage";
    }
}
