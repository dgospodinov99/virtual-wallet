package com.team01.web.virtualwallet.models.dto;

import com.team01.web.virtualwallet.models.enums.UserSortOptions;

import java.util.Optional;

public class FilterUserParams {

    private Optional<String> username;

    private Optional<String> email;

    private Optional<String> phoneNumber;

    private Optional<UserSortOptions> sortParam;

    public FilterUserParams() {
    }

    public FilterUserParams setUsername(String username) {
        this.username = Optional.ofNullable(username);
        return this;
    }

    public FilterUserParams setEmail(String email) {
        this.email = Optional.ofNullable(email);
        return this;
    }

    public FilterUserParams setPhoneNumber(String phoneNumber) {
        this.phoneNumber = Optional.ofNullable(phoneNumber);
        return this;
    }

    public FilterUserParams setSortParam(UserSortOptions sortParam) {
        this.sortParam = Optional.ofNullable(sortParam);
        return this;
    }

    public Optional<String> getUsername() {
        return username;
    }

    public Optional<String> getEmail() {
        return email;
    }

    public Optional<String> getPhoneNumber() {
        return phoneNumber;
    }

    public Optional<UserSortOptions> getSortParam() {
        return sortParam;
    }
}