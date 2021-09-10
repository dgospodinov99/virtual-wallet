package com.team01.web.virtualwallet.models.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class TransferDto {

    @NotNull
    private int senderId;

    @NotNull
    private int receiverId;

    @Positive
    private Double amount;

    public TransferDto() {
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
