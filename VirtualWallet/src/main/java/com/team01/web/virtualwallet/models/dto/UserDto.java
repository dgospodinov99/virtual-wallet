package com.team01.web.virtualwallet.models.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

public class UserDto {

    @Email
    private String email;

    @NotBlank
    @Size(min = 10, max = 10,message = "Phone number must be 10 digits long!")
    private String phoneNumber;

    @NotBlank
    @Size(min = 8,max = 100, message = "Password must be at least 8 symbols!")
    private String password;

    @NotBlank
    private String photoURL;

    @Positive
    private double balance;

    public UserDto(){}

    public UserDto(String email, String phoneNumber, String password, String photoURL, double balance) {
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.photoURL = photoURL;
        this.balance = balance;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
