package com.team01.web.virtualwallet.models.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.util.List;

public class UserDto {

    @NotBlank
    @Size(min = 2,max = 20,message = "Username must be between 2 and 20 symbols!")
    private String username;

    @Email
    private String email;

    @NotBlank
    @Size(min = 10, max = 10,message = "Phone number must be 10 digits long!")
    private String phoneNumber;

    @NotBlank
    @Size(min = 8,max = 100, message = "Password must be at least 8 symbols!")
    private String password;

    private String photoURL;

    @Positive
    private double balance;

    private List<Integer> cardsId;

    public UserDto(){}

    public UserDto(String username,
                   String email,
                   String phoneNumber,
                   String password,
                   String photoURL,
                   double balance,
                   List<Integer> cardsId) {
        this.username = username;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.photoURL = photoURL;
        this.balance = balance;
        this.cardsId = cardsId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public List<Integer> getCardsId() {
        return cardsId;
    }

    public void setCardsId(List<Integer> cardsId) {
        this.cardsId = cardsId;
    }
}
