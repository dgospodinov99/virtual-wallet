package com.team01.web.virtualwallet.services;

import com.team01.web.virtualwallet.config.EmailConfig;
import com.team01.web.virtualwallet.models.Token;
import com.team01.web.virtualwallet.services.contracts.EmailService;
import com.team01.web.virtualwallet.services.contracts.TokenService;
import com.team01.web.virtualwallet.services.contracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.internet.InternetAddress;
import java.util.Random;

@Service
public class EmailServiceImpl implements EmailService {

    private final EmailConfig emailConfig;
    private final JavaMailSender mailSender;
    private final TokenService tokenService;
    private final UserService userService;

    @Autowired
    public EmailServiceImpl(EmailConfig emailConfig, JavaMailSender mailSender, TokenService tokenService, UserService userService) {
        this.emailConfig = emailConfig;
        this.mailSender = mailSender;
        this.tokenService = tokenService;
        this.userService = userService;
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
    public Token sendVerifyRegistrationEmail(String recipientEmail) {
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();

        String token = random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
        SimpleMailMessage toSend = new SimpleMailMessage();
        String url="http://localhost:8080/auth/verify/" + token;
        String content="<a href='"+url+"'>"+url+"</a>";
        String message = "Please click on the following link to complete your registration: \n" + content;
        MimeMessagePreparator preparator = mimeMessage -> {
            mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(recipientEmail));
            mimeMessage.setFrom(new InternetAddress(emailConfig.getUsername()));
            mimeMessage.setSubject("Email Verification");
            mimeMessage.setText(message, "UTF-8", "html");
        };
        Token tokenToSave = new Token();
        tokenToSave.setActive(true);
        tokenToSave.setUser(userService.getByEmail(recipientEmail));
        tokenToSave.setToken(token);
        tokenService.create(tokenToSave);
        mailSender.send(preparator);
        return tokenToSave;
    }

    @Override
    public void sendVerifyTransactionEmail(String recipientEmail, int code) {
        String message = "Please confirm large transaction using the code: " + code;
        sendSimpleMessage(recipientEmail, "Large Transaction Verification", message);
    }

}
