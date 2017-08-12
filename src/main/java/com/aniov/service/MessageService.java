package com.aniov.service;

import com.aniov.model.Message;
import com.aniov.model.Profile;
import com.aniov.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service class for Message
 */
@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    public Message saveMessage(String message, Profile toProfile, Profile fromProfile) {

        Message newMessage = new Message(message, toProfile, fromProfile);
        return messageRepository.save(newMessage);
    }

    /**
     * Check if user can send message to another one
     *
     * @param from logged user
     * @param to user to receive message
     * @return true if condition is satisfied
     */
    public boolean canMessageBeSent(Profile from, Profile to) {
        return  (from.getLikesReceived().contains(to) && to.getLikesReceived().contains(from));
    }
}
