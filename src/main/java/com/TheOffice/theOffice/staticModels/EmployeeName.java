package com.TheOffice.theOffice.staticModels;

public class EmployeeName {
    private long id;
    private String name;
    private String gender;

    public EmployeeName() {
    }

    public EmployeeName(long id, String name, String gender) {
        this.id = id;
        this.name = name;
        this.gender = gender;
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
}
