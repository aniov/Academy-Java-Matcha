package com.aniov.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * User Profile entity class
 */
@Entity
@Data
public class Profile implements Serializable{

    @Id
    @GeneratedValue(generator = "myGenerator")
    @GenericGenerator(name = "myGenerator", strategy = "foreign", parameters = @Parameter(name = "property", value = "user"))
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
    private Gender lookingFor;

    @Enumerated(EnumType.STRING)
    private SexualOrientation sexualOrientation;

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

    private enum SexualOrientation {
        HETEROSEXUAL, BI_SEXUAL, GAY
    }

    private enum Gender {
        MALE, FEMALE
    }
}
