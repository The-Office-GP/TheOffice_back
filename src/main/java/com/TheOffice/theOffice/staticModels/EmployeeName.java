package com.TheOffice.theOffice.staticModels;

public class EmployeeName {
    private long id;
    private String name;
    private String gender;
    private String path;

    public EmployeeName() {
    }

    public EmployeeName(long id, String name, String gender, String path) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.path = path;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
