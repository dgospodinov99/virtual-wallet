package com.team01.web.virtualwallet.models.dto;

import javax.validation.constraints.Positive;

public class CreateTransferDto {

    @Positive(message = "Sender ID is invalid!")
    private int senderId;

    @Positive(message = "Receiver ID is invalid!")
    private int receiverId;

    @Positive(message = "Transaction amount must be positive!")
    private Double amount;

    public CreateTransferDto() {
    }

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public int getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(int receiverId) {
        this.receiverId = receiverId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}
