package com.aniov.model.dto;

import com.aniov.model.Interest;
import com.aniov.model.Picture;
import com.aniov.model.Profile;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * DTO for user profile
 */
@Data
@NoArgsConstructor
public class ProfileDTO implements Serializable{

    private String username;
    private String aboutMe;
    private String whatImDoing;
    private String goodAt;
    private String favorites;

    @Size(min = 3, max = 50)
    private String firstName;

    @Size(min = 3, max = 50)
    private String lastName;

    @Size(min = 3, max = 50)
    private String country;

    @Size(min = 3, max = 50)
    private String town;

    private Date bornDate;

    private Profile.Gender gender;

    private Profile.Gender lookingFor;

    private Profile.SexualOrientation sexualOrientation;

    private Profile.Status status;

   /* private List<Picture> pictures;

    private Set<Interest> interests;*/

    public ProfileDTO(Profile profile) {
        this.username = profile.getUser().getUsername();
        this.aboutMe = profile.getAboutMe();
        this.whatImDoing = profile.getWhatImDoing();
        this.goodAt = profile.getGoodAt();
        this.favorites = profile.getFavorites();
        this.firstName = profile.getFirstName();
        this.lastName = profile.getLastName();
        this.country = profile.getCountry();
        this.town = profile.getTown();
        this.bornDate = profile.getBornDate();
        this.gender = profile.getGender();
        this.lookingFor = profile.getLookingFor();
        this.sexualOrientation = profile.getSexualOrientation();
        this.status = profile.getStatus();
       /* this.pictures = profile.getPictures();
        this.interests = profile.getInterests();*/

    }
}
