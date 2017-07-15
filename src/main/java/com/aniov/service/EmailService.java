package com.aniov.service;

import lombok.Setter;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.StringWriter;
import java.util.Date;
import java.util.Properties;

/**
 * Email service
 */

@Service
@Setter
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${site.url}")
    private String url;

    @Async
    private void sendEmail(String to, String subject, String body) throws MessagingException {

        MimeMessage message = mailSender.createMimeMessage();

        MimeMessageHelper messageHelper = new MimeMessageHelper(message, true);

        messageHelper.setSubject(subject);
        messageHelper.setTo(to);
        messageHelper.setFrom("matcha@matcha-dating.com");
        messageHelper.setSentDate(new Date());
        messageHelper.setText(body, true);

        mailSender.send(message);
    }

    public void sendRegistrationToken(String to, String token) throws MessagingException {
        String subject = "Activate your account";

        Properties properties = new Properties();
        properties.setProperty("resource.loader", "class");
        properties.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        Velocity.init(properties);

        VelocityContext context = new VelocityContext();
        context.put("token", token);
        context.put("url", url);

        Template template = Velocity.getTemplate("templates/activateaccountemail.vm");

        StringWriter sw = new StringWriter();
        template.merge(context, sw);//We merge the context(body of our message) to a StringWriter - sw

        sendEmail(to, subject, sw.toString());
    }

    public void resetPasswordToken(String to, String token) throws MessagingException {
        String subject = "Reset your password";

        Properties properties = new Properties();
        properties.setProperty("resource.loader", "class");
        properties.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        Velocity.init(properties);

        VelocityContext context = new VelocityContext();
        context.put("token", token);
        context.put("url", url);

        Template template = Velocity.getTemplate("templates/resetpasswordemail.vm");

        StringWriter sw = new StringWriter();
        template.merge(context, sw);//We merge the context(body of our message) to a StringWriter - sw

        sendEmail(to, subject, token);
    }
}
