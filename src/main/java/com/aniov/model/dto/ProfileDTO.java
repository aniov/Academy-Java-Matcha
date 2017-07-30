package com.aniov.model.dto;

import com.aniov.model.Profile;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * DTO for user profile
 */
@Data
@NoArgsConstructor
public class ProfileDTO implements Serializable {

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

    private String gender;

    private Set<String> lookingFor = new HashSet<>();

    private String status;

    //@Range(min = 120, max = 230)
    private Integer height;

    private String bodyType;

    private String ethnicity;

    private String sexualOrientation;

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

    }
}
