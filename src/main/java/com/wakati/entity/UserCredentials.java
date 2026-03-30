package com.wakati.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "USER_CREDENTIALS")
public class UserCredentials extends BaseUpdatedAtEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;

    @Column(name = "Password")
    private String password;

    private String pinHash;

    private String passwordAlgo;
    private String pinAlgo;

    private String passwordUpdatedAt;
    private String pinUpdatedAt;

    private String failedAttempts;
    private String lockedUntil;

    private String createdBy;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPinHash() {
        return pinHash;
    }

    public void setPinHash(String pinHash) {
        this.pinHash = pinHash;
    }

    public String getPasswordAlgo() {
        return passwordAlgo;
    }

    public void setPasswordAlgo(String passwordAlgo) {
        this.passwordAlgo = passwordAlgo;
    }

    public String getPinAlgo() {
        return pinAlgo;
    }

    public void setPinAlgo(String pinAlgo) {
        this.pinAlgo = pinAlgo;
    }

    public String getPasswordUpdatedAt() {
        return passwordUpdatedAt;
    }

    public void setPasswordUpdatedAt(String passwordUpdatedAt) {
        this.passwordUpdatedAt = passwordUpdatedAt;
    }

    public String getPinUpdatedAt() {
        return pinUpdatedAt;
    }

    public void setPinUpdatedAt(String pinUpdatedAt) {
        this.pinUpdatedAt = pinUpdatedAt;
    }

    public String getFailedAttempts() {
        return failedAttempts;
    }

    public void setFailedAttempts(String failedAttempts) {
        this.failedAttempts = failedAttempts;
    }

    public String getLockedUntil() {
        return lockedUntil;
    }

    public void setLockedUntil(String lockedUntil) {
        this.lockedUntil = lockedUntil;
    }


    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

}