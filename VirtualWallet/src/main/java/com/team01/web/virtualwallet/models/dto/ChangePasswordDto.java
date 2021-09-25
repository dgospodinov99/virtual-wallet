package com.team01.web.virtualwallet.models.dto;

import javax.validation.constraints.Size;

public class ChangePasswordDto {

    private String oldPassword;

    @Size(min = 8, max = 100, message = "Password must be at least 8 symbols!")
    private String newPassword;

    private String newPasswordConfirm;

    public ChangePasswordDto() {
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getNewPasswordConfirm() {
        return newPasswordConfirm;
    }

    public void setNewPasswordConfirm(String newPasswordConfirm) {
        this.newPasswordConfirm = newPasswordConfirm;
    }
}
