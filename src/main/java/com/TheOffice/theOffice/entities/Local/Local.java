package com.TheOffice.theOffice.entities.Local;

import java.math.BigDecimal;

public class Local {
    private Long id;
    private LocalLevel level;
    private Integer size;
    private BigDecimal rent;
    private Integer maxEmployees;
    private Integer maxMachines;
    private byte[] background_image;
    private Long id_company;

    public Local() {
    }

    public Local(Long id, LocalLevel level, Integer size, BigDecimal rent, Integer maxEmployees, Integer maxMachines, byte[] background_image, Long id_company) {
        this.id = id;
        this.level = level;
        this.size = size;
        this.rent = rent;
        this.maxEmployees = maxEmployees;
        this.maxMachines = maxMachines;
        this.background_image = background_image;
        this.id_company = id_company;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalLevel getLevel() { return level; }
    public void setLevel(LocalLevel level) { this.level = level; }

    public Integer getSize() { return size; }
    public void setSize(Integer size) { this.size = size; }

    public BigDecimal getRent() { return rent; }
    public void setRent(BigDecimal rent) { this.rent = rent; }

    public Integer getMaxEmployees() { return maxEmployees; }
    public void setMaxEmployees(Integer maxEmployees) { this.maxEmployees = maxEmployees; }

    public Integer getMaxMachines() { return maxMachines; }
    public void setMaxMachines(Integer maxMachines) { this.maxMachines = maxMachines; }

    public byte[] getBackground_image() {
        return background_image;
    }

    public void setBackground_image(byte[] background_image) {
        this.background_image = background_image;
    }

    public Long getId_company() { return id_company; }
    public void setId_company(Long id_company) { this.id_company = id_company; }
}

