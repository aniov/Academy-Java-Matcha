package com.aniov.service;

import com.aniov.model.Message;
import com.aniov.model.Profile;
import com.aniov.model.dto.MessageDTO;
import com.aniov.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Service class for Message
 */
@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    public void saveMessage(String message, Profile toProfile, Profile fromProfile) {

        Message newMessage = new Message(message, toProfile, fromProfile);
        messageRepository.save(newMessage);
    }

    /**
     * Get auth user all messages using Pageable
     *
     * @param profile auth user profile
     * @return Page<Message> containing all sent and received messages
     */
    public Page<MessageDTO> getAllMessages(Profile profile) {
        //We request our page result to be ordered by created date descending
        Pageable request = new PageRequest(0, 20, Sort.Direction.DESC, "createDate");

        Page<Message> messages = messageRepository.findBySentFromProfileOrSentToProfile(profile, profile, request);
        List<MessageDTO> messageDTOS = new ArrayList<>();

        for (Message message : messages) {
            messageDTOS.add(new MessageDTO(message, profile.getUser().getUsername()));
        }

        return new PageImpl<>(messageDTOS, request, messages.getTotalElements());
    }

    /**
     * Check if user can send message to another one
     *
     * @param from logged user
     * @param to   user to receive message
     * @return true if condition is satisfied
     */
    public boolean canMessageBeSent(Profile from, Profile to) {
        return (from.getLikesReceived().contains(to) && to.getLikesReceived().contains(from));
    }
}
