package com.team01.web.virtualwallet.services.contracts;

import com.team01.web.virtualwallet.models.Token;
import org.springframework.mail.SimpleMailMessage;

public interface EmailService {

    Token sendVerifyRegistrationEmail(String recipientEmail);

    Token sendVerifyTransactionEmail(String recipientEmail);

}
