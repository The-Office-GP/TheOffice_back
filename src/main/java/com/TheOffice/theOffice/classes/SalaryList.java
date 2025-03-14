package com.TheOffice.theOffice.classes;

import java.util.List;

public class SalaryList {
    private List<Salary> salaryList;

    public SalaryList(){

    }

    public SalaryList(List<Salary> salaryList) {
        this.salaryList = salaryList;
    }

    public List<Salary> getSalaryList() {
        return salaryList;
    }

    public void setSalaryList(List<Salary> salaryList) {
        this.salaryList = salaryList;
    }

    @Override
    public String toString() {
        return "SalaryList{" +
                "salary=" + salaryList +
                '}';
    }
}
