package com.team01.web.virtualwallet.models.dto;


import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

public class TransferDto {


    @Positive(message = "Wallet ID is invalid!")
    private int walletId;

    @Positive(message = "Card ID is invalid!")
    private int cardId;

    @Positive(message = "Amount is invalid!")
    private double amount;

    private String timestamp;

    public TransferDto() {
    }

    public int getWalletId() {
        return walletId;
    }

    public void setWalletId(int walletId) {
        this.walletId = walletId;
    }

    public int getCardId() {
        return cardId;
    }

    public void setCardId(int cardId) {
        this.cardId = cardId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
