package com.team01.web.virtualwallet.models.dto;

import javax.validation.constraints.Positive;
import java.time.LocalDateTime;
import java.util.Optional;

public class FilterTransactionByAdminParams {

    @Positive
    private Optional<Integer> senderId;

    @Positive
    private Optional<Integer> receiverId;

    @Positive
    private Optional<String> sortParam;

    private Optional<LocalDateTime> startDate;

    private Optional<LocalDateTime> endDate;

    public FilterTransactionByAdminParams() {
    }

    public FilterTransactionByAdminParams setSenderId(Integer senderId) {
        this.senderId = Optional.ofNullable(senderId);
        return this;
    }

    public FilterTransactionByAdminParams setReceiverId(Integer receiverId) {
        this.receiverId = Optional.ofNullable(receiverId);
        return this;
    }

    public FilterTransactionByAdminParams setSortParam(String sortParam) {
        this.sortParam = Optional.ofNullable(sortParam);
        return this;
    }

    public FilterTransactionByAdminParams setStartDate(Optional<LocalDateTime> startDate) {
        this.startDate = startDate;
        return this;
    }

    public FilterTransactionByAdminParams setEndDate(Optional<LocalDateTime> endDate) {
        this.endDate = endDate;
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

    public Optional<LocalDateTime> getStartDate() {
        return startDate;
    }

    public Optional<LocalDateTime> getEndDate() {
        return endDate;
    }
}
