package com.aniov.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

/**
 * Account entity class
 */
@Data
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

    @NotBlank
    private boolean enabled;

    @NotBlank
    private boolean accountNonExpired = true;

    @NotBlank
    private boolean credentialsNonExpired = true;

    @NotBlank
    private boolean accountNonLocked = true;

    @NotBlank
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern="yyyy/MM/dd hh:mm:ss")
    private Date created;

    @NotBlank
    @OneToOne(cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private User user;

    @PrePersist
    protected void onCreate() {
        if (created == null)
            created = new Date();
    }


}
