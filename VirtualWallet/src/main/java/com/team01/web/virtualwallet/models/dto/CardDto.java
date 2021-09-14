package com.team01.web.virtualwallet.models.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;

public class CardDto {

    @NotBlank(message = "Card Number can't be blank!")
    @Size(min = 16, max = 16, message = "Card number be 16 digits")
    private String cardNumber;

    @NotBlank(message = "Card Holder can't be blank!")
    @Size(min = 2, max = 20, message = "Card holder must be between 2 and 20 symbols")
    private String holder;

    @NotBlank(message = "Card Check Number can't be blank!")
    @Size(min = 3, max = 3, message = "Card check number must be between 3 digits")
    private String checkNumber;

    @Positive(message = "User ID must be positive!")
    private int userId;

    @JsonFormat(pattern="MM/yyyy")
    private LocalDate expirationDate;

    public CardDto() {
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public CardDto setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
        return this;
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
