package com.aniov.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * Email service
 */

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Async
    private void sendEmail(String to, String subject, String body) throws MessagingException {

        MimeMessage message = mailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setSubject(subject);
        helper.setTo(to);
        helper.setText(body, true);

        mailSender.send(message);
    }

    public void sendRegistrationToken(String to, String token) throws MessagingException {
        String subject = "Activate your account";
        sendEmail(to, subject, token);
    }

    public void resetPasswordToken(String to, String token) throws MessagingException {
        String subject = "Reset your password";
        sendEmail(to, subject, token);
    }
}
