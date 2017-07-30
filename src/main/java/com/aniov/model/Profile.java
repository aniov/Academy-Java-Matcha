package com.aniov.model;

import com.aniov.model.dto.ProfileDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * User Profile entity class
 */
@Entity
@Data
public class Profile implements Serializable {

    @Id
    @GeneratedValue(generator = "myGenerator")
    @GenericGenerator(name = "myGenerator", strategy = "foreign", parameters = @Parameter(name = "property", value = "user"))
    @JsonIgnore
    private Long id;

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

    @Temporal(TemporalType.TIMESTAMP)
    private Date bornDate;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    @ElementCollection(targetClass = Gender.class)
    @PrimaryKeyJoinColumn
    private Set<Gender> lookingFor;

    @Enumerated(EnumType.STRING)
    private SexualOrientation sexualOrientation;

    //@Range(min = 120, max = 230)
    private Integer height;

    @Enumerated(EnumType.STRING)
    private BodyType bodyType;

    @Enumerated(EnumType.STRING)
    private ETHNICITY ethnicity;

    @Enumerated(EnumType.STRING)
    private Status status;

    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL)
    private List<Picture> pictures;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "profile_interest",
            joinColumns = @JoinColumn(name = "profile_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "interest_id", referencedColumnName = "id"))
    @JsonManagedReference
    private Set<Interest> interests;

    @NotNull
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @PrimaryKeyJoinColumn
    @JsonIgnore
    private User user;

    public void edit(ProfileDTO profileDTO) {
        this.aboutMe = profileDTO.getAboutMe();
        this.whatImDoing = profileDTO.getWhatImDoing();
        this.goodAt = profileDTO.getGoodAt();
        this.favorites = profileDTO.getFavorites();
        this.firstName = profileDTO.getFirstName();
        this.lastName = profileDTO.getLastName();
        this.country = profileDTO.getCountry();
        this.town = profileDTO.getTown();
        this.bornDate = profileDTO.getBornDate();
        if (profileDTO.getGender() != null) {
            this.gender = Gender.valueOf(profileDTO.getGender().toUpperCase());
        }
        if (!profileDTO.getLookingFor().isEmpty()) {
            this.lookingFor = new HashSet<>();
            for (String gender : profileDTO.getLookingFor()) {
                this.lookingFor.add(Gender.valueOf(gender.toUpperCase()));
            }
        }
        if (profileDTO.getStatus() != null) {
            this.status = Status.values()[Status.position(profileDTO.getStatus())];
        }
        setSexualOrientation();

    }

    @Getter
    @AllArgsConstructor
    public enum SexualOrientation {
        HETEROSEXUAL("Heterosexual"), BI_SEXUAL("Bi-sexual"), HOMOSEXUAL("Homosexual");
        private String sexualType;
    }

    @Getter
    @AllArgsConstructor
    public enum Gender {
        MAN("Man"), WOMAN("Woman");
        private String genderType;
    }

    @Getter
    @AllArgsConstructor
    public enum Status {
        NOT_SAYING("Not Saying"), SINGLE("Single"), IN_A_RELATION("In a relation"), MARRIED("Married");
        private String status;

        private static int position(String status) {
            for (int i = 0; i < Status.values().length; i++) {
                if (status.equalsIgnoreCase(Status.values()[i].getStatus())) {
                    return i;
                }
            }
            return -1;
        }
    }

    @Getter
    @AllArgsConstructor
    public enum BodyType {
        SLIM("Slim"), ATHLETIC("Athletic"), AVERAGE("Average"), CURVY("Curvy");
        private String body;
    }

    @Getter
    @AllArgsConstructor
    public enum ETHNICITY {
        ASIAN("Asian"), BLACK_AFRICAN("Black/African"), INDIAN("Indian"), LATINO_HISPANIC("Latino/Hispanic"),
        MIDDLE_EASTERN("Middle Eastern"), WHITE_CAUCASIAN("White/Caucasian"), MIXED_OTHER("Mixed/Other");
        private String ethnicity;
    }

    private void setSexualOrientation() {
        if (gender != null && lookingFor != null) {
            if (gender.genderType.equalsIgnoreCase(Gender.WOMAN.genderType)) {
                if (lookingFor.contains(Gender.WOMAN) && lookingFor.contains(Gender.MAN)) {
                    sexualOrientation = SexualOrientation.BI_SEXUAL;
                } else if (lookingFor.contains(Gender.WOMAN)) {
                    sexualOrientation = SexualOrientation.HOMOSEXUAL;
                } else if (lookingFor.contains(Gender.MAN)) {
                    sexualOrientation = SexualOrientation.HETEROSEXUAL;
                }
            } else if (gender.genderType.equalsIgnoreCase(Gender.MAN.genderType)) {
                if (lookingFor.contains(Gender.WOMAN) && lookingFor.contains(Gender.MAN)) {
                    sexualOrientation = SexualOrientation.BI_SEXUAL;
                } else if (lookingFor.contains(Gender.MAN)) {
                    sexualOrientation = SexualOrientation.HOMOSEXUAL;
                } else if (lookingFor.contains(Gender.WOMAN)) {
                    sexualOrientation = SexualOrientation.HETEROSEXUAL;
                }
            }
        }
    }

}
