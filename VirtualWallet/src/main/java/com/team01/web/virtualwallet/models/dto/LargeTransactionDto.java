package com.team01.web.virtualwallet.models.dto;

public class LargeTransactionDto {

    int receiverId;

    double amount;

    String token;

    public LargeTransactionDto() {}

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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
