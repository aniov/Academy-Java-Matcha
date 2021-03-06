package com.aniov.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Logout success handler
 */
@Component
public class MyLogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler {

    //Using MyHttpSessionEventPublisher
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        this.logger.warn("Logout success: " + response + "; logout: " + authentication);
        super.onLogoutSuccess(request, response, authentication);
    }
}
