package com.team01.web.virtualwallet.models.dto;

import javax.validation.constraints.Positive;

public class CreateTransactionDto {

    @Positive(message = "Transaction amount must be positive!")
    private double amount;

    @Positive(message = "Receiver ID is invalid!")
    private int receiverId;

    public CreateTransactionDto() {
    }

    public int getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(int receiverId) {
        this.receiverId = receiverId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
