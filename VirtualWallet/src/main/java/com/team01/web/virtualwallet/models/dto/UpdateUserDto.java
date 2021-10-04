package com.team01.web.virtualwallet.models.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

public class UpdateUserDto {

    @NotBlank(message = "Please enter a valid email!")
    @Email(message = "Please enter a valid email!")
    private String email;

    @Pattern(regexp = "[0-9]+", message = "Phone number must contain only digits")
    @Size(min = 10, max = 10, message = "Phone number must be 10 digits long!")
    private String phoneNumber;


    public UpdateUserDto() {
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
}
