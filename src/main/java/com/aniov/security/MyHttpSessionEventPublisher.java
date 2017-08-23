package com.aniov.security;

import com.aniov.model.Profile;
import com.aniov.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSessionEvent;
import java.util.Date;

import static com.aniov.security.MyAuthenticationSuccessHandler.SESSION_USERNAME_ATR;

@Component
public class MyHttpSessionEventPublisher extends HttpSessionEventPublisher {

    @Autowired
    private ProfileService profileService;

    @Override
    public void sessionCreated(HttpSessionEvent event) {
        //We set our inactive session interval
        event.getSession().setMaxInactiveInterval(30 * 60); //in seconds => 30 min
        super.sessionCreated(event);
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent event) {

        //When session is expired we set on user profile last time online
        Profile profile = profileService.findByUserName(event.getSession().getAttribute(SESSION_USERNAME_ATR).toString());
        profile.setLastOnline(new Date());
        profileService.saveProfileEntity(profile);

        super.sessionDestroyed(event);
    }
}
