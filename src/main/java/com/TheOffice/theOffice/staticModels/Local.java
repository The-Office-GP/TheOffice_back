package com.TheOffice.theOffice.staticModels;

public class Local {
    public Long id;
    public String level;
    public Long size;
    public Long rent;
    public Long maxEmployees;
    public Long maxMachines;
    public String pathBackgroundImage;

    public Local(){
    }

    public Local(Long id, String level, Long size, Long rent, Long maxEmployees, Long maxMachines, String pathBackgroundImage) {
        this.id = id;
        this.level = level;
        this.size = size;
        this.rent = rent;
        this.maxEmployees = maxEmployees;
        this.maxMachines = maxMachines;
        this.pathBackgroundImage = pathBackgroundImage;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public Long getRent() {
        return rent;
    }

    public void setRent(Long rent) {
        this.rent = rent;
    }

    public Long getMaxEmployees() {
        return maxEmployees;
    }

    public void setMaxEmployees(Long maxEmployees) {
        this.maxEmployees = maxEmployees;
    }

    public Long getMaxMachines() {
        return maxMachines;
    }

    public void setMaxMachines(Long maxMachines) {
        this.maxMachines = maxMachines;
    }

    public String getPathBackgroundImage() {
        return pathBackgroundImage;
    }

    public void setPathBackgroundImage(String pathBackgroundImage) {
        this.pathBackgroundImage = pathBackgroundImage;
    }
}