package com.aniov.security;

import com.aniov.model.Profile;
import com.aniov.model.SiteUserDetails;
import com.aniov.service.ProfileService;
import com.aniov.utils.WebSocketTransmit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

/**
 * Logout success handler
 */
@Component
public class MyLogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler {

    @Autowired
    private WebSocketTransmit webSocketTransmit;

    @Autowired
    private ProfileService profileService;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        this.logger.warn("Logout success: " + response + "; logout: " + authentication);
        SiteUserDetails siteUserDetails = (SiteUserDetails) authentication.getPrincipal();
        Profile profile = profileService.findByUserName(siteUserDetails.getUsername());
        profile.setLastOnline(new Date());
        profileService.saveProfileEntity(profile);

        request.logout();
        super.onLogoutSuccess(request, response, authentication);
    }
}
