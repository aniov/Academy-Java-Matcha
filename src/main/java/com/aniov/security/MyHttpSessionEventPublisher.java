package com.aniov.security;

import com.aniov.model.Profile;
import com.aniov.service.ProfileService;
import com.aniov.utils.WebSocketTransmit;
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

    @Autowired
    private WebSocketTransmit webSocketTransmit;

    @Override
    public void sessionCreated(HttpSessionEvent event) {
        //We set our inactive session interval
        event.getSession().setMaxInactiveInterval(60 * 60); //in seconds => 60 min
        super.sessionCreated(event);
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent event) {

        //When session is expired we set on user profile last time online
        String username = event.getSession().getAttribute(SESSION_USERNAME_ATR).toString();
        Profile profile = profileService.findByUserName(username);
        profile.setLastOnline(new Date());
        profileService.saveProfileEntity(profile);

        //WebSocket logged out
        webSocketTransmit.linkedUserHasLoggedOut(username);
        webSocketTransmit.userHasLogged(username, false);

        event.getSession().invalidate();
        super.sessionDestroyed(event);
    }
}
