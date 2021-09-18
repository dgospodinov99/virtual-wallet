package com.team01.web.virtualwallet.services;

import com.team01.web.virtualwallet.config.EmailConfig;
import com.team01.web.virtualwallet.services.contracts.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    private final EmailConfig emailConfig;


    private final JavaMailSender mailSender;

    @Autowired
    public EmailServiceImpl(EmailConfig emailConfig, JavaMailSender mailSender) {
        this.emailConfig = emailConfig;
        this.mailSender = mailSender;
    }

    @Override
    public SimpleMailMessage sendSimpleMessage(String to, String subject, String text) {

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(emailConfig.getUsername());
        mailMessage.setTo(to);
        mailMessage.setSubject(subject);
        mailMessage.setText(text);
        mailSender.send(mailMessage);
        return mailMessage;
    }

    @Override
    public void sendVerifyRegistrationEmail(String recipientEmail, int code) {
        String message = "Your verification code is: " + code;
        sendSimpleMessage(recipientEmail, "Registration Verification", message);
    }

    @Override
    public void sendVerifyTransactionEmail(String recipientEmail, int code) {
        String message = "Please confirm large transaction using the code: " + code;
        sendSimpleMessage(recipientEmail, "Large Transaction Verification", message);
    }

}
