package com.team01.web.virtualwallet.models.dto;

public class DummyDto {

    private String cardNumber;

    private String cardCheck;

    private String expirationDate;

    private double amount;

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardCheck() {
        return cardCheck;
    }

    public void setCardCheck(String cardCheck) {
        this.cardCheck = cardCheck;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
