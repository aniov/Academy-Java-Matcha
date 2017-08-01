package com.aniov.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by Marius on 7/12/2017.
 */
@Entity
@Data
public class Picture implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String pictureName;

    private boolean isProfilePicture;

    @Lob
    private byte[] pictureData;

    private Integer pictureHeight;

    private Integer pictureWidth;

    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    @ManyToOne(cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    @JsonIgnore
    private Profile profile;
}
