package com.team01.web.virtualwallet.services;

import com.team01.web.virtualwallet.config.EmailConfig;
import com.team01.web.virtualwallet.services.contracts.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    private final EmailConfig emailConfig;

    @Autowired
    public EmailServiceImpl(EmailConfig emailConfig) {
        this.emailConfig = emailConfig;
    }

    @Override
    public void sendSimpleMessage(String to, String subject, String text) {

        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(emailConfig.getHost());
        mailSender.setPort(emailConfig.getPort());
        mailSender.setUsername(emailConfig.getUsername());
        mailSender.setPassword(emailConfig.getPassword());

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom("NoReply@VirtualWalletDB.com");
        mailMessage.setTo(to);
        mailMessage.setSubject(subject);
        mailMessage.setText(text);
        mailSender.send(mailMessage);
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
