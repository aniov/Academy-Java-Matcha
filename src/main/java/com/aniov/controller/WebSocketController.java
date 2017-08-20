package com.aniov.controller;

import com.aniov.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;

/**
 * Currently not used, Will be used when chat is implemented
 */
@Controller
public class WebSocketController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private UserService userService;

    /**
     * A Messaging mapping to send messages to all users
     *
     * @param principal
     * @throws Exception
     */
    @MessageMapping("/hello")
    @SendTo("/queue/greetings")
    public void greeting(Principal principal) throws Exception {
        /*String time = new SimpleDateFormat("HH:mm").format(new Date());
        String text = "Hy From: " + principal.getName();
        for (String username : usersToBeNotify) {
            simpMessagingTemplate.convertAndSendToUser(username, "/queue/like", principal.getName() + " has Clicked");
        }
        return new ResponseEntity<>(text + " at: " + time, HttpStatus.OK);*/
    }

}
