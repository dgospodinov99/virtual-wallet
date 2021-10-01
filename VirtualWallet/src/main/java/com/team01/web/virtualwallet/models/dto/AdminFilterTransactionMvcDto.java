package com.team01.web.virtualwallet.models.dto;

public class AdminFilterTransactionMvcDto {

    private Integer senderId;

    private Integer receiverId;

    private String startDate;

    private String endDate;


    public AdminFilterTransactionMvcDto() {
    }

    public AdminFilterTransactionMvcDto setSenderId(Integer senderId) {
        this.senderId = senderId;
        return this;
    }

    public AdminFilterTransactionMvcDto setReceiverId(Integer receiverId) {
        this.receiverId = receiverId;
        return this;
    }

    public AdminFilterTransactionMvcDto setStartDate(String startDate) {
        this.startDate = startDate;
        return this;
    }

    public AdminFilterTransactionMvcDto setEndDate(String endDate) {
        this.endDate = endDate;
        return this;
    }

    public Integer getSenderId() {
        return senderId;
    }

    public Integer getReceiverId() {
        return receiverId;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }
}
