package com.aniov.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Navigation between pages
 */

@Controller
public class NavigationController {

    @GetMapping(path = "/")
    public String login() {
        return "loginpage.html";
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
}
