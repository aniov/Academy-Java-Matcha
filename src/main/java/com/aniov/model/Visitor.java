package com.aniov.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Entity
@Getter
@Setter
public class Visitor implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @NonNull
    @JsonBackReference
    private Profile profile;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Date visitDate;

    private boolean seenThis;
}
