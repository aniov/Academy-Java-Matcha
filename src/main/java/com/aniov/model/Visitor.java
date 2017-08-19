package com.aniov.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@NoArgsConstructor
public class Visitor implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @NonNull
    @JsonBackReference
    private Profile profile;

    @NonNull
    private String whoVisit;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Date visitDate;

    private boolean seenThis;

    @PrePersist
    protected void onCreate() {
        if (visitDate == null)
            visitDate = new Date();
    }
}
