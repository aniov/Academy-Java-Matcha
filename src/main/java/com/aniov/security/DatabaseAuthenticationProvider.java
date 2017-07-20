package com.aniov.security;

import com.aniov.model.User;
import com.aniov.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * Check if the user name / password provided by the user are found in DB
 */

@Service
public class DatabaseAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserService userService;

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken) throws AuthenticationException {
    }

    @Override
    protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken) throws AuthenticationException {

        boolean valid = true;

        final String plainPassword = usernamePasswordAuthenticationToken.getCredentials().toString();

        /** Check if the "plainPassword" String is null || contain only white spaces */
        if (!StringUtils.hasText(plainPassword)) {
            this.logger.warn("AccountName {}: no password provided", username);
            valid = false;
        }
        User user = userService.findUserByUserName(username);

        if (user == null) {
            this.logger.warn("UserName {}: user not found", username);
            valid = false;
        } else {

            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
            if (!bCryptPasswordEncoder.matches(plainPassword, user.getHashedPassword())) {
                this.logger.warn("userName {}: bad password for user", username);
                valid = false;
            }
        }

        if (!valid) {
            throw new BadCredentialsException("Invalid User name / Password for user: " + username);
        }
        return userService.loadUserByUsername(user.getUsername());
    }
}
