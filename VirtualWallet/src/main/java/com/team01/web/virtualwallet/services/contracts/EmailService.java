package com.team01.web.virtualwallet.services.contracts;

public interface EmailService {

    void sendSimpleMessage(String to, String subject, String text);

    void sendVerifyRegistrationEmail(String recipientEmail, int code);

    void sendVerifyTransactionEmail(String recipientEmail, int code);

}
