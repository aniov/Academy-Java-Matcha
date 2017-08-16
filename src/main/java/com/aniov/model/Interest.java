package com.aniov.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

/**
 * Interests of a user
 */
@Entity
@Getter
@Setter
@RequiredArgsConstructor
@NoArgsConstructor
public class Interest implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonIgnore
    private Long id;

    @NonNull
    private String interest;

    @ManyToMany(mappedBy = "interests", fetch = FetchType.LAZY)
    @JsonBackReference
    private Set<Profile> profile;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Interest interest1 = (Interest) o;

        return interest != null ? interest.equals(interest1.interest) : interest1.interest == null;
    }

    @Override
    public int hashCode() {
        return interest != null ? interest.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Interest{" +
                "id=" + id +
                ", interest='" + interest + '\'';
    }
}
