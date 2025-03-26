package com.TheOffice.theOffice.entities;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;
import java.util.Date;

public class Company {
    private Long id;
    @NotBlank (message = "Le secteur de l'entreprise ne peut pas être vide")
    private String sector;
    @NotBlank (message = "Le nom de l'entreprise ne peut pas être vide")
    private String name;
    private LocalDate creationDate;
    @Max(100)
    private Long popularity;
    private Long localId;
    private Long machineId;
    private Long userId;

    public Company (){
    }

    public Company(Long id, String sector, String name, LocalDate creationDate,Long popularity, Long localId,Long machineId, Long userId) {
        this.id = id;
        this.sector = sector;
        this.name = name;
        this.creationDate = creationDate;
        this.popularity = popularity;
        this.localId = localId;
        this.machineId = machineId;
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

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public Long getPopularity() {
        return popularity;
    }

    public void setPopularity(Long popularity) {
        this.popularity = popularity;
    }

    public Long getLocalId() {
        return localId;
    }

    public void setLocalId(Long localId) {
        this.localId = localId;
    }

    public Long getMachineId() {
        return machineId;
    }

    public void setMachineId(Long machineId) {
        this.machineId = machineId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
