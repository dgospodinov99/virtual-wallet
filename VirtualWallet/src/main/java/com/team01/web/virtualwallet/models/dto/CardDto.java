package com.team01.web.virtualwallet.models.dto;

import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

public class CardDto {

    @NotBlank
    @Size(min = 16, max = 16, message = "Card number be 16 digits")
    private String cardNumber;

    @NotBlank
    @Size(min = 2, max = 20, message = "Card holder must be between 2 and 20 symbols")
    private String holder;

    @NotBlank
    @Size(min = 3, max = 3, message = "Card check number must be between 3 digits")
    private String checkNumber;

    //userId = walletId
    @Positive
    private int userId;

    public CardDto() {
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

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
