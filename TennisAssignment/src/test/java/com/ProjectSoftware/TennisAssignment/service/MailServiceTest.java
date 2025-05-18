package com.ProjectSoftware.TennisAssignment.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private MailService mailService;

    @Test
    void testSendRegistrationDecision() {
        mailService.sendRegistrationDecision(
            "test@example.com",
            "John Doe",
            "Wimbledon",
            "ACCEPTED"
        );

        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }
}
