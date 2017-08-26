package com.aniov.utils;

import com.aniov.model.Profile;
import com.aniov.model.SiteUserDetails;
import com.aniov.model.User;
import com.aniov.model.dto.IsOnlineSocketDTO;
import com.aniov.model.dto.ProfileLikeSocketDTO;
import com.aniov.model.dto.ReceivedMessageDTO;
import com.aniov.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Service;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Service
public class WebSocketTransmit {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private ProfileService profileService;

    @Autowired
    private SessionRegistry sessionRegistry;

    public void linkedUserHasLoggedOut(String username) {

        linkedUserHasLogged(username, false);
    }

    public void linkedUserHasLoggedIn(String username) {

        linkedUserHasLogged(username, true);
    }

    /**
     * Sends over WebSocket to all linked users who logged in / out
     *
     * @param username who logged in / out
     * @param isLogged true / false
     */
    private void linkedUserHasLogged(String username, boolean isLogged) {

        Profile profile = profileService.findByUserName(username);

        Set<Profile> likesGiven = profile.getLikesGiven();
        Set<Profile> likesReceived = profile.getLikesReceived();

        // -> Only linkedProfiles
        likesGiven.retainAll(likesReceived);

        for (Profile userProfile : likesGiven) {
            simpMessagingTemplate.convertAndSendToUser(userProfile.getUser().getUsername(), "/queue/online", new IsOnlineSocketDTO(username, isLogged));
        }
    }

    /**
     * Sends over webSocket to all logged users who logged in / out
     *
     * @param username who logged in / out
     * @param isLogged true / false
     */
    public void userHasLogged(String username, boolean isLogged) {

        List<Object> principals = sessionRegistry.getAllPrincipals();
        Set<User> loggedUsers = new LinkedHashSet<>();

        for (Object principal : principals) {
            if (principal instanceof SiteUserDetails) {
                loggedUsers.add(((SiteUserDetails) principal).getUser());
            }
        }

        for (User user : loggedUsers) {
            simpMessagingTemplate.convertAndSendToUser(user.getUsername(), "/queue/online-all", new IsOnlineSocketDTO(username, isLogged));
        }
    }

    /**
     * Sends message over WebSocket to the Like / Un-liked user
     *
     * @param from auth username
     * @param to   username of notify user
     * @param like true / false
     */
    public void sendLikeOverSocket(String from, String to, boolean like) {
        simpMessagingTemplate.convertAndSendToUser(to, "/queue/like", new ProfileLikeSocketDTO(from, like));
    }

    /**
     * Sends message over WebSocket to user receiving new message
     *
     * @param from    auth username
     * @param to      username of notify user
     * @param message body of the message
     */
    public void sendMessageInfoOverSocket(String from, String to, String message) {
        simpMessagingTemplate.convertAndSendToUser(to, "/queue/message", new ReceivedMessageDTO(from, message));
    }

    /**
     * Logout username
     *
     * @param logOutUsername username to be logged out
     */
    public void forceLogoutUser(String logOutUsername) {
        simpMessagingTemplate.convertAndSendToUser(logOutUsername, "/queue/logout", true);
    }
}
