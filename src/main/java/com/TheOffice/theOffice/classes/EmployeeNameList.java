package com.TheOffice.theOffice.classes;

import java.util.List;

public class EmployeeNameList {
    private List<EmployeeName> nameList;

    public EmployeeNameList() {
    }

    public EmployeeNameList(List<EmployeeName> nameList) {
        this.nameList = nameList;
    }

    public List<EmployeeName> getNameList() {
        return nameList;
    }

    public void setNameList(List<EmployeeName> nameList) {
        this.nameList = nameList;
    }

    @Override
    public String toString() {
        return "EmployeeList{" +
                "employees=" + nameList +
                '}';
    }
}
