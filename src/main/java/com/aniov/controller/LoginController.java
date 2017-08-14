package com.aniov.controller;

import com.aniov.model.SiteUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Login page Controller
 */

@Controller
public class LoginController {

    @Autowired
    private SessionRegistry sessionRegistry;

    @GetMapping(path = "/login")
    public String login() {
        return "loginpage";
    }

    /* At logout we remove user from session*/
    @GetMapping("/logout")
    public String logout(HttpServletRequest request, Authentication authentication) throws ServletException {

        if (authentication != null) {
            Object principal = authentication.getPrincipal();

            if (principal instanceof SiteUserDetails) {
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
