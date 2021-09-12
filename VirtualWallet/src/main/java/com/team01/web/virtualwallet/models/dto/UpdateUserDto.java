package com.team01.web.virtualwallet.models.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

public class UpdateUserDto {

    @Email
    private String email;

    @NotBlank
    @Size(min = 10, max = 10,message = "Phone number must be 10 digits long!")
    private String phoneNumber;

    @NotBlank
    @Size(min = 8,max = 100, message = "Password must be at least 8 symbols!")
    private String password;

    private String photoURL;

    private List<Integer> cardsId;

    public UpdateUserDto(){}

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

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }
}
