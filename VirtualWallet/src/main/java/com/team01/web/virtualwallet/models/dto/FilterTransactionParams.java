package com.team01.web.virtualwallet.models.dto;

import com.team01.web.virtualwallet.models.TransactionDirection;

import javax.validation.constraints.Positive;
import java.util.Optional;

public class FilterTransactionParams {

    @Positive
    private Optional<Integer> senderUsername;

    @Positive
    private Optional<Integer> receiverUsername;

    @Positive
    private Optional<String> sortParam;

    private Optional<TransactionDirection> direction;

    public FilterTransactionParams() {
    }

    public FilterTransactionParams setSenderUsername(Integer senderUsername) {
        this.senderUsername = Optional.ofNullable(senderUsername);
        return this;
    }

    public FilterTransactionParams setReceiverUsername(Integer receiverUsername) {
        this.receiverUsername = Optional.ofNullable(receiverUsername);
        return this;
    }

    public FilterTransactionParams setSortParam(String sortParam) {
        this.sortParam = Optional.ofNullable(sortParam);
        return this;
    }

    public FilterTransactionParams setDirection(String direction) {
        if(direction != null) {
            this.direction = Optional.of(TransactionDirection.valueOf(direction));
        }else {
            this.direction = Optional.empty();
        }
        return this;
    }

    public Optional<Integer> getSenderUsername() {
        return senderUsername;
    }

    public Optional<Integer> getReceiverUsername() {
        return receiverUsername;
    }

    public Optional<String> getSortParam() {
        return sortParam;
    }

    public Optional<TransactionDirection> getDirection() {
        return direction;
    }

}
