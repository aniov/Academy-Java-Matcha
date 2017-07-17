package com.aniov.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

/**
 * Interests of a user
 */
@Entity
@Data
public class Interest implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String interest;

    @ManyToMany(mappedBy = "interests", fetch = FetchType.LAZY)
    @JsonBackReference
    private Set<Profile> profile;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Interest interest1 = (Interest) o;

        return interest != null ? interest.equals(interest1.interest) : interest1.interest == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (interest != null ? interest.hashCode() : 0);
        return result;
    }
}
