package com.wakati.model.request;

public class IslandRegionRequest {

    private String action;

    // island
    private String code;
    private String nameEn;
    private String nameFr;
    private String nameKm;
    private String status;

    // region
    private String islandCode;
    private String regionEn;
    private String regionFr;
    private String regionKm;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIslandCode() {
        return islandCode;
    }

    public void setIslandCode(String islandCode) {
        this.islandCode = islandCode;
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
}