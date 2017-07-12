package com.aniov.model;

import lombok.Data;

import javax.persistence.*;
import java.lang.reflect.Type;
import java.util.Date;

/**
 * Created by Marius on 7/12/2017.
 */
@Entity
@Data
public class Picture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String pictureName;

    private boolean isProfilePicture;

    @Lob
    private byte[] pictureData;

    private Date date;

    @ManyToOne(cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private Profile profile;
}
