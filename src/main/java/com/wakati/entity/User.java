package com.wakati.entity;

import com.wakati.enums.*;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "USERS")
public class User extends BaseUpdatedAtEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_id", unique = true, nullable = false)
    private String userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_type")
    private UserType userType;

    private String fullName;
    private String mobileNo;
    private String email;

    private String island;

    @ManyToOne
    @JoinColumn(name = "region", referencedColumnName = "code")
    private Region region;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Enumerated(EnumType.STRING)
    private RegistrationStage registrationStage;

    @Enumerated(EnumType.STRING)
    private ManagedByType managedByType;

    private String managedById;
    private String createdBy;

    private String lastLogin;

    @Enumerated(EnumType.STRING)
    private VerificationStatus verifiedByAdmin;

    private String adminId;

    @Enumerated(EnumType.STRING)
    private VerificationStatus verifiedByAdjudicator;

    private String adjudicatorId;

    private String fcmToken;
    private String rejectedReason;

    private LocalDateTime verifiedAt;

    private String sessionToken;
    private String preferredLanguage;

    // Relationships
    @OneToMany(mappedBy = "owner")
    private List<Wallet> wallets;

    @OneToMany(mappedBy = "sourceUser")
    private List<Transaction> sourceTransactions;

    @OneToMany(mappedBy = "targetUser")
    private List<Transaction> targetTransactions;

    @OneToMany(mappedBy = "user",fetch = FetchType.LAZY)
    private List<UserAttributes> attributes;

    @OneToMany(mappedBy = "user",fetch = FetchType.LAZY)
    private List<UserDocuments> documents;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIsland() {
        return island;
    }

    public void setIsland(String island) {
        this.island = island;
    }

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public RegistrationStage getRegistrationStage() {
        return registrationStage;
    }

    public void setRegistrationStage(RegistrationStage registrationStage) {
        this.registrationStage = registrationStage;
    }

    public ManagedByType getManagedByType() {
        return managedByType == null ? ManagedByType.PLATFORM : managedByType;
    }

    public void setManagedByType(ManagedByType managedByType) {
        this.managedByType = managedByType;
    }

    public String getManagedById() {
        return managedById;
    }

    public void setManagedById(String managedById) {
        this.managedById = managedById;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(String lastLogin) {
        this.lastLogin = lastLogin;
    }

    public VerificationStatus getVerifiedByAdmin() {
        return verifiedByAdmin;
    }

    public void setVerifiedByAdmin(VerificationStatus verifiedByAdmin) {
        this.verifiedByAdmin = verifiedByAdmin;
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public VerificationStatus getVerifiedByAdjudicator() {
        return verifiedByAdjudicator;
    }

    public void setVerifiedByAdjudicator(VerificationStatus verifiedByAdjudicator) {
        this.verifiedByAdjudicator = verifiedByAdjudicator;
    }

    public String getAdjudicatorId() {
        return adjudicatorId;
    }

    public void setAdjudicatorId(String adjudicatorId) {
        this.adjudicatorId = adjudicatorId;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    public String getRejectedReason() {
        return rejectedReason;
    }

    public void setRejectedReason(String rejectedReason) {
        this.rejectedReason = rejectedReason;
    }

    public LocalDateTime getVerifiedAt() {
        return verifiedAt;
    }

    public void setVerifiedAt(LocalDateTime verifiedAt) {
        this.verifiedAt = verifiedAt;
    }

    public String getSessionToken() {
        return sessionToken;
    }

    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }

    public String getPreferredLanguage() {
        return preferredLanguage;
    }

    public void setPreferredLanguage(String preferredLanguage) {
        this.preferredLanguage = preferredLanguage;
    }

    public List<Wallet> getWallets() {
        return wallets;
    }

    public void setWallets(List<Wallet> wallets) {
        this.wallets = wallets;
    }

    public List<Transaction> getSourceTransactions() {
        return sourceTransactions;
    }

    public void setSourceTransactions(List<Transaction> sourceTransactions) {
        this.sourceTransactions = sourceTransactions;
    }

    public List<Transaction> getTargetTransactions() {
        return targetTransactions;
    }

    public void setTargetTransactions(List<Transaction> targetTransactions) {
        this.targetTransactions = targetTransactions;
    }
}