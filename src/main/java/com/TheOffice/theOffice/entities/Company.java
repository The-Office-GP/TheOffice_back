package com.TheOffice.theOffice.entities;

import jakarta.validation.constraints.NotBlank;

import java.util.Date;
import java.util.List;

public class Company {
    Long id;
    String sector;
    @NotBlank (message = "Le nom de l'entreprise ne peut pas être vide")
    String name;
    Date creation_date;
    Long id_user;
    List<Machine> machines;

    public Company (){
    }

    public Company(Long id, String sector, String name, Date creation_date, Long id_user, List<Machine> machines) {
        this.id = id;
        this.sector = sector;
        this.name = name;
        this.creation_date = creation_date;
        this.id_user = id_user;
        this.machines = machines;
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

    public Date getCreation_date() {
        return creation_date;
    }

    public void setCreation_date(Date creation_date) {
        this.creation_date = creation_date;
    }

    public Long getId_user() {
        return id_user;
    }

    public void setId_user(Long id_user) {
        this.id_user = id_user;
    }

    public List<Machine> getMachines() {return machines;}

    public void setMachines(List<Machine> machines) {this.machines = machines;}
}
