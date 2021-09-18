package com.team01.web.virtualwallet.services.contracts;

import org.springframework.mail.SimpleMailMessage;

public interface EmailService {

    SimpleMailMessage sendSimpleMessage(String to, String subject, String text);

    void sendVerifyRegistrationEmail(String recipientEmail, int code);

    void sendVerifyTransactionEmail(String recipientEmail, int code);

}
