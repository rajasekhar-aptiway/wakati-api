package com.wakati.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "REGIONS")
public class Region {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "code")
    private String code;
    @Column(name = "island")
    private String island;
    @Column(name = "regionEn")
    private String regionEn;
    @Column(name = "regionFr")
    private String regionFr;
    @Column(name = "regionKm")
    private String regionKm;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "region")
    private List<User> users;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getIsland() {
        return island;
    }

    public void setIsland(String island) {
        this.island = island;
    }

    public String getRegionEn() {
        return regionEn;
    }

    public void setRegionEn(String regionEn) {
        this.regionEn = regionEn;
    }

    public String getRegionFr() {
        return regionFr;
    }

    public void setRegionFr(String regionFr) {
        this.regionFr = regionFr;
    }

    public String getRegionKm() {
        return regionKm;
    }

    public void setRegionKm(String regionKm) {
        this.regionKm = regionKm;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}