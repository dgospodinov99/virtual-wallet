package com.team01.web.virtualwallet.models.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;


public class CreateCardDto {

    @Size(min = 16, max = 16, message = "Card number must be 16 digits")
    private String cardNumber;

    @Size(min = 2, max = 20, message = "Card holder must be between 2 and 20 symbols")
    private String holder;

    @Size(min = 3, max = 3, message = "Card check number must be 3 digits")
    private String checkNumber;

    @NotBlank(message = "Card expiration date can't be blank!")
    private String expirationDate;

    public CreateCardDto() {
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getHolder() {
        return holder;
    }

    public void setHolder(String holder) {
        this.holder = holder;
    }

    public String getCheckNumber() {
        return checkNumber;
    }

    public void setCheckNumber(String checkNumber) {
        this.checkNumber = checkNumber;
    }
}
