package com.team01.web.virtualwallet.models.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

public class UpdateUserDto {

    @Email(message = "Please enter a valid email!")
    private String email;

    @NotBlank(message = "Phone number can't be blank!")
    @Size(min = 10, max = 10, message = "Phone number must be 10 digits long!")
    private String phoneNumber;


    public UpdateUserDto() {
    }

    public String getEmail() {
        return email;
    }

    public UpdateUserDto setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public UpdateUserDto setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }
}
