package com.team01.web.virtualwallet.services.contracts;

public interface EmailService {

    void sendSimpleMessage(String to, String subject, String text);

}
