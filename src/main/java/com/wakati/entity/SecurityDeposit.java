package com.wakati.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "SECURITY_DEPOSITS")
public class SecurityDeposit extends BaseCreatedAtEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code", length = 100)
    private String code;

    @Column(name = "type_of_dealer", length = 100)
    private String typeOfDealer;

    @Column(name = "description", length = 100)
    private String description;

    @Column(name = "status", length = 100)
    private String status;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTypeOfDealer() {
        return typeOfDealer;
    }

    public void setTypeOfDealer(String typeOfDealer) {
        this.typeOfDealer = typeOfDealer;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
