package com.aniov.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.Set;

/**
 * Created by Marius on 7/12/2017.
 */
@Entity
@Data
public class Authority implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private AuthorityType authorityType;

    @ManyToMany(mappedBy = "authorities", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonBackReference
    private Set<Account> accounts;

    @Override
    public String getAuthority() {
        return authorityType.name();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Authority authority = (Authority) o;

        return authorityType == authority.authorityType;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (authorityType != null ? authorityType.hashCode() : 0);
        return result;
    }
}
