package com.aniov.model.dto;

import com.aniov.model.Picture;
import com.aniov.model.Profile;
import com.aniov.model.Visitor;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class VisitorDTO implements Serializable {

    private Long id;

    private String username;

    private String address;

    private byte[] mainPhoto;

    private Date date;

    public VisitorDTO(Profile profile, Visitor visitor) {
        this.id = visitor.getId();
        this.username = visitor.getWhoVisit();
        this.address = profile.getAddress();
        if (profile.getPictures() != null) {
            this.mainPhoto = getMainPhoto(profile);
        }
        this.date = visitor.getVisitDate();
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
}
