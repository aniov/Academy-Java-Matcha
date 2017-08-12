package com.aniov.model.dto;

import com.aniov.model.Message;
import com.aniov.model.Picture;
import com.aniov.model.Profile;
import lombok.Data;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * A DTO class for managing all massages received
 */
@Data
public class MessageDTO {

    private String message;

    private String username;

    //if it's not sentMessage then is received message
    private boolean sentMessage;

    private String created;

    private byte[] profilePhoto;

    public MessageDTO(Message message, String currentUsername) {

        this.message = message.getMessage();
        this.created = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(message.getCreateDate());

        if (currentUsername.equals(message.getSentFromProfile().getUser().getUsername())) {
            sentMessage = true;
            this.username = message.getSentToProfile().getUser().getUsername();
            this.profilePhoto = getMainPhoto(message.getSentToProfile());

        } if (currentUsername.equals(message.getSentToProfile().getUser().getUsername())) {
            sentMessage = false;
            this.username = message.getSentFromProfile().getUser().getUsername();
            this.profilePhoto = getMainPhoto(message.getSentFromProfile());
        }
    }

    private byte[] getMainPhoto(Profile profile) {
        List<Picture> pictures = profile.getPictures();
        for (Picture picture : pictures) {
            if (picture.isProfilePicture()) {
                return picture.getPictureData();
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "MessageDTO{" +
                "message='" + message + '\'' +
                ", username='" + username + '\'' +
                ", sentMessage=" + sentMessage +
                '}';
    }
}
