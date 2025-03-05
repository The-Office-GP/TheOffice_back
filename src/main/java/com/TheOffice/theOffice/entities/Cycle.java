package com.TheOffice.theOffice.entities;

public class Cycle {
    private Long id;
    private Double cost;
    private Long employees;
    private Long productivity;
    private Long popularity;
    private Long step;
    private Long id_company;

    public Cycle(){}

    public Cycle(Long id, Double cost, Long employees, Long productivity, Long popularity, Long step, Long id_company){
        this.id = id;
        this.cost = cost;
        this.employees = employees;
        this.productivity = productivity;
        this.popularity = popularity;
        this.step = step;
        this.id_company = id_company;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public Long getEmployees() {
        return employees;
    }

    public void setEmployees(Long employees) {
        this.employees = employees;
    }

    public Long getProductivity() {
        return productivity;
    }

    public void setProductivity(Long productivity) {
        this.productivity = productivity;
    }

    public Long getPopularity() {
        return popularity;
    }

    public void setPopularity(Long popularity) {
        this.popularity = popularity;
    }

    public Long getStep() {
        return step;
    }

    public void setStep(Long step) {
        this.step = step;
    }

    public Long getId_company() {
        return id_company;
    }

    public void setId_company(Long id_company) {
        this.id_company = id_company;
    }
}
