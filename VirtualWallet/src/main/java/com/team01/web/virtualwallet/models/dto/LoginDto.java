package com.team01.web.virtualwallet.models.dto;

import javax.validation.constraints.NotBlank;

public class LoginDto {

    @NotBlank(message = "Username can't be blank!")
    private String username;

    @NotBlank(message = "Password can't be blank!")
    private String password;

    public LoginDto() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String email) {
        this.username = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}