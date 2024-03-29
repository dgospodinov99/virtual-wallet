package com.team01.web.virtualwallet.models.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

public class CreateUserDto {

    @NotBlank(message = "Username can't be blank!")
    @Size(min = 2, max = 20, message = "Username must be between 2 and 20 symbols!")
    private String username;

    @Email(message = "Please enter a valid email!")
    private String email;

    @NotBlank(message = "Phone Number can't be blank!")
    @Size(min = 10, max = 10, message = "Phone number must be 10 digits long!")
    private String phoneNumber;

    @NotBlank(message = "Password can't be blank!")
    @Size(min = 8, max = 100, message = "Password must be at least 8 symbols!")
    private String password;

    private List<Integer> cardsId;

    public CreateUserDto() {
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

    public List<Integer> getCardsId() {
        return cardsId;
    }

    public void setCardsId(List<Integer> cardsId) {
        this.cardsId = cardsId;
    }
}
