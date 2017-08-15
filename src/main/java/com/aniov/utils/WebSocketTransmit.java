package com.aniov.utils;

import com.aniov.model.Profile;
import com.aniov.model.dto.IsOnlineSocketDTO;
import com.aniov.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WebSocketTransmit {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private ProfileService profileService;

    public void userHasLoggedOut(String username) {

        userHasLogged(username, false);
    }

    public void userHasLoggedIn(String username) {

        userHasLogged(username, true);
    }

    private void userHasLogged(String username, boolean isLogged) {

        Profile profile = profileService.findByUserName(username);

        List<Profile> likesGiven = profile.getLikesGiven();
        List<Profile> likesReceived = profile.getLikesReceived();

        // -> Only linkedProfiles
        likesGiven.retainAll(likesReceived);

        for (Profile userProfile : likesGiven) {
            simpMessagingTemplate.convertAndSendToUser(userProfile.getUser().getUsername(), "/queue/online", new IsOnlineSocketDTO(username, isLogged));
        }

    }
}
