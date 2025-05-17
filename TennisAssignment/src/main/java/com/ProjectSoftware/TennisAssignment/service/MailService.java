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
        String subject = "Tournament Registration - " + tournamentName;
        String body = String.format(
            "Hello %s,\n\nYour registration for the tournament '%s' has been %s.\n\nGood luck!\n",
            playerName, tournamentName, status.toLowerCase()
        );

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
    }
}
