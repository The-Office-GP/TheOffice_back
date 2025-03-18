package com.TheOffice.theOffice.entities;

import jakarta.validation.constraints.NotBlank;

import java.util.Date;

public class Company {
    private Long id;
    @NotBlank (message = "Le secteur de l'entreprise ne peut pas être vide")
    private String sector;
    @NotBlank (message = "Le nom de l'entreprise ne peut pas être vide")
    private String name;
    private Date creationDate;
    private Long localId;
    private Long userId;

    public Company (){
    }

    public Company(Long id, String sector, String name, Date creationDate, Long localId, Long userId) {
        this.id = id;
        this.sector = sector;
        this.name = name;
        this.creationDate = creationDate;
        this.localId = localId;
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Long getLocalId() {
        return localId;
    }

    public void setLocalId(Long localId) {
        this.localId = localId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
