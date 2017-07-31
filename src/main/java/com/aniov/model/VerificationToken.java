package com.aniov.model;

import lombok.Data;
import lombok.NoArgsConstructor;
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
@NoArgsConstructor
@RequiredArgsConstructor
public class VerificationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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
    @ManyToOne
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
