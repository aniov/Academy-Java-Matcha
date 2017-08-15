package com.aniov.controller;

import com.aniov.model.Message;
import com.aniov.model.Profile;
import com.aniov.model.dto.GenericResponseDTO;
import com.aniov.model.dto.MessageDTO;
import com.aniov.model.dto.ReceivedMessageDTO;
import com.aniov.service.MessageService;
import com.aniov.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Handle messages between users
 */
@RestController
public class MessageController {

    @Autowired
    private UserService userService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    /**
     * Records a message send by authProfile to another Profile
     * The message to be saved in DB both sender and receiver need to like each other profile
     *
     * @param message  text message to be saved
     * @param username Profile username to receive the message
     * @return HttpStatus.OK if message was successfully saved
     */
    @PostMapping(path = "/user/send-message", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addMessageToUser(@RequestBody @Valid ReceivedMessageDTO message, @RequestParam(name = "name") String username) {

        if (username == null || userService.findUserByUserName(username) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String authUsername = auth.getName();

        Profile fromProfile = userService.findUserByUserName(authUsername).getProfile();
        Profile toProfile = userService.findUserByUserName(username).getProfile();
        if (username.equals(authUsername) || !messageService.canMessageBeSent(fromProfile, toProfile)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        messageService.saveMessage(message.getMessage(), toProfile, fromProfile);
        sendMessageInfoOverSocket(authUsername, username, message.getMessage());

        return new ResponseEntity<>(new GenericResponseDTO("Message added"), HttpStatus.OK);
    }

    /**
     * Get all received messages
     *
     * @return received messages
     */
    @GetMapping(path = "/user/messages")
    public ResponseEntity<?> getReceivedMessages() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String authUsername = auth.getName();
        Profile authProfile = userService.findUserByUserName(authUsername).getProfile();

        List<Message> messages = authProfile.getReceivedMessages();
        return new ResponseEntity<>(messages, HttpStatus.OK);
    }

    /**
     * Get all messages from auth user using pagination
     *
     * @return received and send messages by page
     */
    @GetMapping(path = "/user/all-messages")
    public ResponseEntity<?> getAllUserMessages(@RequestParam(name = "page") int pageNumber) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String authUsername = auth.getName();
        Profile authProfile = userService.findUserByUserName(authUsername).getProfile();

        Page<MessageDTO> messageDTOS = messageService.getAllMessages(authProfile, pageNumber);

        return new ResponseEntity<Object>(messageDTOS, HttpStatus.OK);
    }

    /**
     * Sends message over WebSocket to user receiving new message
     *
     * @param from    auth username
     * @param to      username of notify user
     * @param message body of the message
     */
    private void sendMessageInfoOverSocket(String from, String to, String message) {
        simpMessagingTemplate.convertAndSendToUser(to, "/queue/message", new ReceivedMessageDTO(from, message));
    }
}
