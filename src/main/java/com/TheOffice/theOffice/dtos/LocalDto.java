package com.TheOffice.theOffice.dtos;

import com.TheOffice.theOffice.entities.Local.Local;
import com.TheOffice.theOffice.entities.Local.LocalLevel;

import java.math.BigDecimal;

public class LocalDto {
    private Long id;
    private LocalLevel level;
    private Integer size;
    private BigDecimal rent;
    private Integer maxEmployees;
    private Integer maxMachines;
    private byte[] background_image;

    public LocalDto() {
    }

    public LocalDto(Long id, LocalLevel level, Integer size, BigDecimal rent, Integer maxEmployees, Integer maxMachines, byte[] background_image) {
        this.id = id;
        this.level = level;
        this.size = size;
        this.rent = rent;
        this.maxEmployees = maxEmployees;
        this.maxMachines = maxMachines;
        this.background_image = background_image;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalLevel getLevel() {
        return level;
    }

    public void setLevel(LocalLevel level) {
        this.level = level;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public BigDecimal getRent() {
        return rent;
    }

    public void setRent(BigDecimal rent) {
        this.rent = rent;
    }

    public Integer getMaxEmployees() {
        return maxEmployees;
    }

    public void setMaxEmployees(Integer maxEmployees) {
        this.maxEmployees = maxEmployees;
    }

    public Integer getMaxMachines() {
        return maxMachines;
    }

    public void setMaxMachines(Integer maxMachines) {
        this.maxMachines = maxMachines;
    }

    public byte[] getBackground_image() {
        return background_image;
    }

    public void setBackground_image(byte[] background_image) {
        this.background_image = background_image;
    }

    public static LocalDto fromEntity(Local local){
        return new LocalDto(local.getId(), local.getLevel(), local.getSize(), local.getRent(), local.getMaxEmployees(), local.getMaxMachines(), local.getBackground_image());
    }
}
