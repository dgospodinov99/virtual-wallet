package com.team01.web.virtualwallet.models.dto;

import com.team01.web.virtualwallet.models.enums.TransactionDirection;

public class FilterTransactionDto {

    private int walletId;

    private String startDate;

    private String endDate;

    private TransactionDirection direction;

    public FilterTransactionDto() {
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public int getWalletId() {
        return walletId;
    }

    public void setWalletId(int walletId) {
        this.walletId = walletId;
    }

    public TransactionDirection getDirection() {
        return direction;
    }

    public void setDirection(TransactionDirection direction) {
        this.direction = direction;
    }
}
