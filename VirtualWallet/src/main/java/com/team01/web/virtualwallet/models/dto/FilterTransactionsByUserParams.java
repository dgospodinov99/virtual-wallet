package com.team01.web.virtualwallet.models.dto;

import com.team01.web.virtualwallet.models.enums.TransactionDirection;

import java.time.LocalDateTime;
import java.util.Optional;

public class FilterTransactionsByUserParams {

    private Optional<Integer> walletId;

    private Optional<LocalDateTime> startDate;

    private Optional<LocalDateTime> endDate;

    private Optional<TransactionDirection> direction;

    public Optional<Integer> getWalletId() {
        return walletId;
    }

    public Optional<LocalDateTime> getStartDate() {
        return startDate;
    }

    public Optional<LocalDateTime> getEndDate() {
        return endDate;
    }

    public Optional<TransactionDirection> getDirection() {
        return direction;
    }

    public FilterTransactionsByUserParams setWalletId(Optional<Integer> walletId) {
        this.walletId = walletId;
        return this;
    }

    public FilterTransactionsByUserParams setStartDate(Optional<LocalDateTime> startDate) {
        this.startDate = startDate;
        return this;
    }

    public FilterTransactionsByUserParams setEndDate(Optional<LocalDateTime> endDate) {
        this.endDate = endDate;
        return this;
    }

    public FilterTransactionsByUserParams setDirection(Optional<TransactionDirection> direction) {
        this.direction = direction;
        return this;
    }
}
