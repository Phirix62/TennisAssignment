package com.ProjectSoftware.TennisAssignment.service;

//imports
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
@Service
public class MailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendRegistrationDecision(String toEmail, String playerName, String tournamentName, String status) {
    try {
        String subject = "Tournament Registration - " + tournamentName;
        String body = String.format(
            "Hello %s,\n\nYour registration for the tournament '%s' has been %s.\n\nGood luck!",
            playerName, tournamentName, status.toLowerCase()
        );

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("no-reply@tennisapp.com"); // ✅ Fixes Mailtrap error
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(body);

        System.out.println("[MailService] Attempting to send email to: " + toEmail);
        mailSender.send(message);
        System.out.println("[MailService] Email sent successfully.");

    } catch (Exception e) {
        System.err.println("[MailService] Failed to send email:");
        e.printStackTrace();
        throw e;
    }
}
}

