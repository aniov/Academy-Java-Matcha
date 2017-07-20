package com.aniov.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Login page Controller
 */

@Controller
public class LoginController {

    @Autowired
    private SessionRegistry sessionRegistry;

    @GetMapping(path = "/login")
    public String login() {

        System.out.println(sessionRegistry.getAllPrincipals().stream()
                .filter(u -> !sessionRegistry.getAllSessions(u, false).isEmpty())
                .map(Object::toString)
                .collect(Collectors.toList()));

        return "loginpage.html";
    }

    /* At logout we remove user from session*/
    @GetMapping("/logout")
    public String logout(HttpServletRequest request, Authentication authentication) throws ServletException {

        if (authentication != null) {
            Object principal = authentication.getPrincipal();

            if (principal instanceof org.springframework.security.core.userdetails.User) {
                List<SessionInformation> userSessions = sessionRegistry.getAllSessions(principal, true);

                for (SessionInformation session : userSessions) {
                    session.expireNow();
                    sessionRegistry.removeSessionInformation(session.getSessionId());
                }
            }
            request.logout();
            request.getSession().invalidate();
        }
        return "redirect:/login";
    }
}
