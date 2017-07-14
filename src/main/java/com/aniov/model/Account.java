package com.aniov.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Account entity class
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
public class Account {

    @Id
    @GeneratedValue(generator = "myGenerator")
    @GenericGenerator(name = "myGenerator", strategy = "foreign", parameters = @Parameter(name = "property", value = "user"))
    private Long id;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "account_authority",
            joinColumns = @JoinColumn(name = "account_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "authority_id", referencedColumnName = "id"))
    @JsonManagedReference
    private Set<Authority> authorities;

    @NotNull
    private boolean enabled = false;

    @NotNull
    private boolean accountNonExpired = true;

    @NotNull
    private boolean credentialsNonExpired = true;

    @NotNull
    private boolean accountNonLocked = true;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy/MM/dd hh:mm:ss")
    private Date created;

    @NotNull
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @PrimaryKeyJoinColumn
    private User user;

    @PrePersist
    protected void onCreate() {
        if (created == null)
            created = new Date();
        //initially we set ROLE_USER to every new Account
        if (authorities == null) {
            authorities = new HashSet<>();
            Authority authority = new Authority();
            authority.setAuthorityType(AuthorityType.ROLE_USER);
            authorities.add(authority);
        }
    }

    public void addAuthority(Authority authority) {
        authorities.add(authority);
    }

    public void removeAuthority(Authority authority) {
        authorities.remove(authority);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Account account = (Account) o;

        return id != null ? id.equals(account.id) : account.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
