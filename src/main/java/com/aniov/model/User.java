package com.aniov.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * User entity class
 */
@Entity
@Setter
@Getter
@NoArgsConstructor
public class User implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Size(min = 5, max = 30)
    @Column(unique = true)
    private String username;

    @NotBlank
    private String hashedPassword;

    @Email
    @NotNull
    @Size(min = 3, max = 50)
    @Column(unique = true)
    private String email;

    @NotNull
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Profile profile;

    @NotNull
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Account account;
}
