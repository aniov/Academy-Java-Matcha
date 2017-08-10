package com.aniov.model.dto;

import com.aniov.model.Profile;
import com.aniov.model.User;
import com.aniov.service.PictureService;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * DTO for user profile
 */
@Data
@NoArgsConstructor
public class ProfileDTO implements Serializable {

    //@Autowired
    private PictureService pictureService = new PictureService();

    private String username;
    private String aboutMe;
    private String whatImDoing;
    private String goodAt;
    private String favorites;

    /* @Null
     @Size(min = 3, max = 50)*/
    private String firstName;

    /*@Null
    @Size(min = 3, max = 50)*/
    private String lastName;

    private String googleLocationID;

    private String address;

    private Date bornDate;

    private String gender;

    private Set<String> lookingFor = new HashSet<>();

    private String status;

    //@Null
    //@Range(min = 120, max = 230)
    private Integer height;

    private String bodyType;

    private String ethnicity;

    private String sexualOrientation;

    private Set<String> likesReceived = new HashSet<>();

    private Set<String> likesGiven = new HashSet<>();

    private byte[] mainPhoto;

    public ProfileDTO(Profile profile) {
        this.username = profile.getUser().getUsername();
        this.aboutMe = profile.getAboutMe();
        this.whatImDoing = profile.getWhatImDoing();
        this.goodAt = profile.getGoodAt();
        this.favorites = profile.getFavorites();
        this.firstName = profile.getFirstName();
        this.lastName = profile.getLastName();
        this.googleLocationID = profile.getGoogleLocationID();
        this.address = profile.getAddress();
        this.bornDate = profile.getBornDate();
        this.height = profile.getHeight();
        if (profile.getGender() != null) {
            this.gender = profile.getGender().getGenderType();
        }
        if (!profile.getLookingFor().isEmpty()) {
            for (Profile.Gender gender : profile.getLookingFor()) {
                lookingFor.add(gender.getGenderType());
            }
        }
        if (profile.getStatus() != null) {
            this.status = profile.getStatus().getStatus();
        }
        if (profile.getSexualOrientation() != null) {
            this.sexualOrientation = profile.getSexualOrientation().getSexualType();
        }
        this.height = profile.getHeight();
        if (profile.getBodyType() != null) {
            this.bodyType = profile.getBodyType().getBody();
        }
        if (profile.getEthnicity() != null) {
            this.ethnicity = profile.getEthnicity().getEthnicity();
        }

        for (Profile prof : profile.getLikesGiven()) {
            this.likesGiven.add(prof.getUser().getUsername());
        }

       // this.likesGiven = profile.getLikesGiven();
        for (Profile prof : profile.getLikesReceived()) {
            this.likesReceived.add(prof.getUser().getUsername());
        }

       // this.likesReceived = profile.getLikesReceived();

        if (pictureService.getMainPhoto(profile) != null) {
            this.mainPhoto = pictureService.getMainPhoto(profile).getPictureData();
        }

    }
}
