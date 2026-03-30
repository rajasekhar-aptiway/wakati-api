package com.wakati.entity;


import com.wakati.enums.Status;
import jakarta.persistence.*;
import com.wakati.enums.IslandStatus;

import java.time.LocalDateTime;

@Entity
@Table(name = "ISLANDS")
public class Island extends BaseCreatedAtEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String code;
    private String nameEn;
    private String nameFr;
    private String nameKm;

    @Enumerated(EnumType.STRING)
    private Status status;



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

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public String getNameFr() {
        return nameFr;
    }

    public void setNameFr(String nameFr) {
        this.nameFr = nameFr;
    }

    public String getNameKm() {
        return nameKm;
    }

    public void setNameKm(String nameKm) {
        this.nameKm = nameKm;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

}