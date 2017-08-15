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

    @Autowired
    private WebSocketTransmit webSocketTransmit;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        this.logger.warn("Authentication success: " + response + "; authentication: " + authentication);

        SiteUserDetails siteUserDetails = (SiteUserDetails) authentication.getPrincipal();
        webSocketTransmit.userHasLoggedIn(siteUserDetails.getUsername());

        super.onAuthenticationSuccess(request, response, authentication);
    }

}
