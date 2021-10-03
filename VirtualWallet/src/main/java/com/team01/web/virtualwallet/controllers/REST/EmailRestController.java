package com.team01.web.virtualwallet.controllers.REST;

import com.team01.web.virtualwallet.controllers.GlobalExceptionHandler;
import com.team01.web.virtualwallet.models.dto.RegisterDto;
import com.team01.web.virtualwallet.services.contracts.EmailService;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/emailSender")
public class EmailRestController {

    private final EmailService emailService;
    private final GlobalExceptionHandler globalExceptionHandler;

    public EmailRestController(EmailService emailService, GlobalExceptionHandler globalExceptionHandler) {
        this.emailService = emailService;
        this.globalExceptionHandler = globalExceptionHandler;
    }

    @PostMapping("/verify-registration")
    public void sendVerification(@Valid @RequestBody RegisterDto dto, BindingResult result) {
//        globalExceptionHandler.checkValidFields(result);
//        emailService.sendVerifyRegistrationEmail(dto.getEmail(), 333333);
    }

//    @PostMapping("/verify-transaction")
//    public SimpleMailMessage sendTransactionVerification() {
//        return emailService.sendSimpleMessage();
//    }
}
