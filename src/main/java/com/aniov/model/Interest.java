package com.aniov.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;

import javax.persistence.*;
import java.util.Set;

/**
 * Interests of a user
 */
@Entity
@Data
public class Interest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String interest;

    @ManyToMany(mappedBy = "interests", fetch = FetchType.LAZY)
    @JsonBackReference
    private Set<Profile> profile;

}
