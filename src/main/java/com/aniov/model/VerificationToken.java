package com.aniov.model;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Calendar;
import java.util.Date;

/**
 * Token for Account activation or User password reset
 */

@Entity
@Data
@RequiredArgsConstructor
public class VerificationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    private String token;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Date expiryDate;

    @NonNull
    @Enumerated(EnumType.STRING)
    private TokenType tokenType;

    @NonNull
    @OneToOne
    @PrimaryKeyJoinColumn
    private User user;

    //We set validity of token for 24h
    @PrePersist
    private void setDate(){
        Calendar c = Calendar.getInstance();
        c.add(Calendar.HOUR, 24);
        expiryDate = c.getTime();
    }
}
