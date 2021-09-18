package com.team01.web.virtualwallet.models.dto;

import com.team01.web.virtualwallet.models.TransactionDirection;

import javax.validation.constraints.Positive;
import java.time.LocalDateTime;
import java.util.Optional;

public class FilterTransactionParams {

    @Positive
    private Optional<Integer> senderId;

    @Positive
    private Optional<Integer> receiverId;

    @Positive
    private Optional<String> sortParam;

    private Optional<TransactionDirection> direction;

    private Optional<LocalDateTime> startDate;

    private Optional<LocalDateTime> endDate;

    public FilterTransactionParams() {
    }

    public FilterTransactionParams setSenderId(Integer senderId) {
        this.senderId = Optional.ofNullable(senderId);
        return this;
    }

    public FilterTransactionParams setReceiverId(Integer receiverId) {
        this.receiverId = Optional.ofNullable(receiverId);
        return this;
    }

    public FilterTransactionParams setSortParam(String sortParam) {
        this.sortParam = Optional.ofNullable(sortParam);
        return this;
    }

    public FilterTransactionParams setStartDate(Optional<LocalDateTime> startDate) {
        this.startDate = startDate;
        return this;
    }

    public FilterTransactionParams setEndDate(Optional<LocalDateTime> endDate) {
        this.endDate = endDate;
        return this;
    }

    public FilterTransactionParams setDirection(String direction) {
        if (direction != null) {
            this.direction = Optional.of(TransactionDirection.valueOf(direction));
        } else {
            this.direction = Optional.empty();
        }
        return this;
    }

    public Optional<Integer> getSenderId() {
        return senderId;
    }

    public Optional<Integer> getReceiverId() {
        return receiverId;
    }

    public Optional<String> getSortParam() {
        return sortParam;
    }

    public Optional<TransactionDirection> getDirection() {
        return direction;
    }

    public Optional<LocalDateTime> getStartDate() {
        return startDate;
    }

    public Optional<LocalDateTime> getEndDate() {
        return endDate;
    }
}
