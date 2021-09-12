package com.team01.web.virtualwallet.models.dto;

import java.util.Optional;

public class FilterUserParams {

    private Optional<String> username;

    private Optional<String> email;

    private Optional<String> phoneNumber;

    public FilterUserParams() {
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

    public FilterUserParams setUsername(String username){
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
}
