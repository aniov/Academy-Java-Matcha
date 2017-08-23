package com.aniov.security;

import com.aniov.model.SiteUserDetails;
import com.aniov.utils.WebSocketTransmit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * A Success Authentication handler
 */
@Component
public class MyAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    public static final String SESSION_USERNAME_ATR = "username";

    @Autowired
    private WebSocketTransmit webSocketTransmit;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        this.logger.warn("Authentication success: " + response + "; authentication: " + authentication);

        SiteUserDetails siteUserDetails = (SiteUserDetails) authentication.getPrincipal();
        webSocketTransmit.linkedUserHasLoggedIn(siteUserDetails.getUsername());
        webSocketTransmit.userHasLogged(siteUserDetails.getUsername(), true);

        //We set username in session - we need it to retrieve it whn session expire
        request.getSession().setAttribute(SESSION_USERNAME_ATR, siteUserDetails.getUsername());

        super.onAuthenticationSuccess(request, response, authentication);
    }
}
