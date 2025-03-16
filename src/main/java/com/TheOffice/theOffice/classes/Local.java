package com.TheOffice.theOffice.classes;

public class Local {
    public Long id;
    public String level;
    public Long size;
    public Long rent;
    public Long max_employees;
    public Long max_machines;
    public String path_background_image;

    public Local(){
    }

    public Local(Long id, String level, Long size, Long rent, Long max_employees, Long max_machines, String path_background_image) {
        this.id = id;
        this.level = level;
        this.size = size;
        this.rent = rent;
        this.max_employees = max_employees;
        this.max_machines = max_machines;
        this.path_background_image = path_background_image;
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

    public Long getMax_employees() {
        return max_employees;
    }

    public void setMax_employees(Long max_employees) {
        this.max_employees = max_employees;
    }

    public Long getMax_machines() {
        return max_machines;
    }

    public void setMax_machines(Long max_machines) {
        this.max_machines = max_machines;
    }

    public String getPath_background_image() {
        return path_background_image;
    }

    public void setPath_background_image(String path_background_image) {
        this.path_background_image = path_background_image;
    }

    @Override
    public String toString() {
        return "Local{id=" + id + ", level='" + level + ", size='" + size + ", rent='" + rent + ", max_employees='" + max_employees + ", max_machines='" + max_machines + ", path_background_image='" + path_background_image + "}";
    }
}