package com.team01.web.virtualwallet.models.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

public class TransactionDto {

    @Positive(message = "Sender ID is invalid!")
    private String sender;

    @Positive(message = "Receiver ID is invalid!")
    private String receiver;

    @Positive(message = "Transaction amount must be positive!")
    private Double amount;

    private String timestamp;

    public TransactionDto() {
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
