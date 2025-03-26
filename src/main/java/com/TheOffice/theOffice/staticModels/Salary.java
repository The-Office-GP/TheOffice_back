package com.TheOffice.theOffice.staticModels;

import java.math.BigDecimal;

public class Salary {
    private int levelSkill;
    private BigDecimal salary;

    public Salary(){
    }

    public Salary(int levelSkill, BigDecimal salary) {
        this.levelSkill = levelSkill;
        this.salary = salary;
    }

    public int getLevelSkill() {
        return levelSkill;
    }

    public void setLevelSkill(int levelSkill) {
        this.levelSkill = levelSkill;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    @Override
    public String toString() {
        return "Employee{levelSkill=" + levelSkill + ", salary='" + salary + "}";
    }
}
