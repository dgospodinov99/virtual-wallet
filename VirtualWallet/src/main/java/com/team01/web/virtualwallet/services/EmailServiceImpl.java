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
import java.time.LocalDateTime;
import java.util.Random;

@Service
public class EmailServiceImpl implements EmailService {

    private final static int TOKEN_EXPIRATION_MINUTES = 30;
    private final static String VERIFICATION_URL = "http://localhost:8080/auth/verify/";
    private final static String VERIFY_EMAIL_SUBJECT = "Email Verification";
    private final static String LARGE_TRANSACTION_VERIFICATION_SUBJECT = "Large Transaction Verification";

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
    public Token sendVerifyRegistrationEmail(String recipientEmail) {
        String token = generateToken();
        String url=VERIFICATION_URL + token;
        String content="<a href='"+url+"'>"+url+"</a>";
        String message = "Please click on the following link to complete your registration: \n" + content;
        MimeMessagePreparator preparator = prepareMessage(recipientEmail, VERIFY_EMAIL_SUBJECT, message);
        Token tokenToSave = new Token();
        tokenToSave.setActive(true);
        tokenToSave.setUser(userService.getByEmail(recipientEmail));
        tokenToSave.setToken(token);
        tokenService.create(tokenToSave);
        mailSender.send(preparator);
        return tokenToSave;
    }

    @Override
    public Token sendVerifyTransactionEmail(String recipientEmail) {
        String token = generateToken();
        String message = "Please confirm large transaction using the code: " + token;
        MimeMessagePreparator preparator = prepareMessage(recipientEmail,
                LARGE_TRANSACTION_VERIFICATION_SUBJECT, message);
        Token tokenToSave = new Token();
        tokenToSave.setActive(true);
        tokenToSave.setExpiration(LocalDateTime.now().plusMinutes(TOKEN_EXPIRATION_MINUTES));
        tokenToSave.setUser(userService.getByEmail(recipientEmail));
        tokenToSave.setToken(token);
        tokenService.create(tokenToSave);
        mailSender.send(preparator);
        return tokenToSave;
    }

    private String generateToken() {
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();

        return random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    public MimeMessagePreparator prepareMessage(String recipient, String subject, String message) {
        return mimeMessage -> {
            mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
            mimeMessage.setFrom(new InternetAddress(emailConfig.getUsername()));
            mimeMessage.setSubject(subject);
            mimeMessage.setText(message, "UTF-8", "html");
        };
    }

}
