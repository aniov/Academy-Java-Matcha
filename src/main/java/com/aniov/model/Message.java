package com.aniov.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
public class Message implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String message;

    @OneToOne
    private User user;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    private boolean isRead;
}
