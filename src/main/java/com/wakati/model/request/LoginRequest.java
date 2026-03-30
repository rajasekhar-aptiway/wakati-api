package com.wakati.model.request;

public class LoginRequest {
    private String mobileNo;
    private String password;
    private String fcmToken;
    private boolean forctLogout;
    private String refreshToken;

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNumber) {
        this.mobileNo = mobileNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    public boolean isForctLogout() {
        return forctLogout;
    }

    public void setForctLogout(boolean forctLogout) {
        this.forctLogout = forctLogout;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}