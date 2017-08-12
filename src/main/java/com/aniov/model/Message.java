package com.aniov.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
public class Message implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonIgnore
    private Long id;

    @NonNull
    private String message;

    @ManyToOne(cascade = CascadeType.ALL)
    @NonNull
    @JsonIgnore
    private Profile sentToProfile;

    @ManyToOne(cascade = CascadeType.ALL)
    @NonNull
    @JsonIgnore
    private Profile sentFromProfile;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    @NotNull
    private boolean isRead;

    @PrePersist
    protected void onCreate() {
        if (createDate == null)
            createDate = new Date();
    }
}
