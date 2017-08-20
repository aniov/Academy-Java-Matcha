package com.aniov.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * Visitor entity
 */
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Visitor visitor = (Visitor) o;

        if (id != null ? !id.equals(visitor.id) : visitor.id != null) return false;
        return profile != null ? profile.equals(visitor.profile) : visitor.profile == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (profile != null ? profile.hashCode() : 0);
        return result;
    }
}
