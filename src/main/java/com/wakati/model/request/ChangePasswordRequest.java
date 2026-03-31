package com.wakati.model.request;

public class ChangePasswordRequest {
    private String userId;
    private String oldPassword;
    private String newPassowrd;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassowrd() {
        return newPassowrd;
    }

    public void setNewPassowrd(String newPassowrd) {
        this.newPassowrd = newPassowrd;
    }
}