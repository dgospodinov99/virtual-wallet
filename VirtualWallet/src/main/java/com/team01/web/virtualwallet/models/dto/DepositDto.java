package com.team01.web.virtualwallet.models.dto;

import javax.validation.constraints.Positive;

public class DepositDto {

    @Positive(message = "Amount must be positive!")
    private double amount;

    private int cardId;

    public DepositDto() {
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getCardId() {
        return cardId;
    }

    public void setCardId(int cardId) {
        this.cardId = cardId;
    }
}
