package com.aniov.service;

import com.aniov.model.TokenType;
import com.aniov.model.User;
import com.aniov.model.VerificationToken;
import com.aniov.repository.VerificationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Verification Token Service
 */

@Service
public class VerificationTokenService {

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    public String createNewUserVerificationToken(User user) {

        VerificationToken token = new VerificationToken(UUID.randomUUID().toString(), TokenType.ACTIVATION, user);
        return verificationTokenRepository.save(token).getToken();
    }

    public String createResetPasswordVerificationToken(User user) {

        VerificationToken token = new VerificationToken(UUID.randomUUID().toString(), TokenType.PASSWORD_RESET, user);
        return verificationTokenRepository.save(token).getToken();
    }

    public void deleteToken(VerificationToken token) {
        verificationTokenRepository.delete(token);
    }

    public VerificationToken getVerificationToken(String token) {
        return verificationTokenRepository.findByToken(token);
    }
}
