package com.team01.web.virtualwallet.models.dto;

import com.team01.web.virtualwallet.models.enums.UserSortOptions;

public class SearchUserMvcDto {

    private String username;

    private String email;

    private String phoneNumber;

    private UserSortOptions sortParam;

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public UserSortOptions getSortParam() {
        return sortParam;
    }

    public SearchUserMvcDto setUsername(String username) {
        this.username = username;
        return this;
    }

    public SearchUserMvcDto setEmail(String email) {
        this.email = email;
        return this;
    }

    public SearchUserMvcDto setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public SearchUserMvcDto setSortParam(UserSortOptions sortParam) {
        this.sortParam = sortParam;
        return this;
    }
}
