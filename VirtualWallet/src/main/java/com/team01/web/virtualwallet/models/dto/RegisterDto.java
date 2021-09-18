package com.team01.web.virtualwallet.models.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

public class RegisterDto extends LoginDto{

    @Email(message = "Please enter a valid email!")
    private String email;

    @NotBlank(message = "Phone Number can't be blank!")
    @Size(min = 10, max = 10, message = "Phone number must be 10 digits long!")
    private String phoneNumber;


    @NotBlank(message = "Password can't be blank!")
    @Size(min = 8, max = 100, message = "Password must be at least 8 symbols!")
    private String confirmPassword;

    public RegisterDto() {
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

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
