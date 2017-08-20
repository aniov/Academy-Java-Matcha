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

    private final int RESULTS_PER_PAGE = 2;

    @Autowired
    private MessageRepository messageRepository;

    /**
     * Saves a new message from fromProfile to toProfile
     *
     * @param message     text message
     * @param toProfile   Profile to whom message is sent
     * @param fromProfile Profile from who message is sent
     */
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
    public Page<MessageDTO> getAllMessages(Profile profile, int pageNumber) {
        //We request our page result to be ordered by created date descending
        Pageable request = new PageRequest(pageNumber, RESULTS_PER_PAGE, Sort.Direction.DESC, "createDate");

        Page<Message> messages = messageRepository.findBySentFromProfileOrSentToProfile(profile, profile, request);
        List<MessageDTO> messageDTOS = new ArrayList<>();

        //We set the messages we gave to front-end as read
        setReceivedMessagesAsRead(messages, profile);

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

    private void setReceivedMessagesAsRead(Page<Message> messages, Profile profile) {
        for (Message message : messages) {
            if (message.getSentToProfile().equals(profile)) {
                message.setRead(true);
                messageRepository.save(message);
            }
        }
    }

}
