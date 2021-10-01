package com.team01.web.virtualwallet.services.contracts;

import com.team01.web.virtualwallet.models.Token;
import org.springframework.mail.SimpleMailMessage;

public interface EmailService {

    SimpleMailMessage sendSimpleMessage(String to, String subject, String text);

    Token sendVerifyRegistrationEmail(String recipientEmail);

    void sendVerifyTransactionEmail(String recipientEmail, int code);

}
