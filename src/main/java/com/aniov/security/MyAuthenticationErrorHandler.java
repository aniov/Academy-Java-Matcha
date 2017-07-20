package com.aniov.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Error Authentication handler
 */
@Component
public class MyAuthenticationErrorHandler extends SimpleUrlAuthenticationFailureHandler{

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        this.logger.warn("Authentication error: " + response + "; exception: " + exception);
        super.onAuthenticationFailure(request, response, exception);
    }
}
