package com.aniov.controller;

import com.aniov.model.User;
import com.aniov.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Controller
public class WebSocketController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private UserService userService;

    @MessageMapping("/hello")
    //@SendTo("/queue/greetings")
    public void greeting(Principal principal) throws Exception {
        Thread.sleep(1000); // simulated delay

        String time = new SimpleDateFormat("HH:mm").format(new Date());

        String text = "Hy From: " + principal.getName();

/*        for (String username : usersToBeNotify) {

            simpMessagingTemplate.convertAndSendToUser(username, "/queue/like", principal.getName() + " has Clicked");
        }*/

      //  return new ResponseEntity<>(text + " at: " + time, HttpStatus.OK);
    }

}
