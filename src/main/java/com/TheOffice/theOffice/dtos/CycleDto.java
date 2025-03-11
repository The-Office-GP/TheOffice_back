package com.TheOffice.theOffice.dtos;

import com.TheOffice.theOffice.entities.Cycle;

public class CycleDto {
    private Long id;
    private Double cost;
    private Long employees;
    private Long productivity;
    private Long popularity;
    private Long step;

    public CycleDto() {}

    // Constructor
    public CycleDto(Long id, Double cost, Long employees, Long productivity, Long popularity, Long step) {
        this.id = id;
        this.cost = cost;
        this.employees = employees;
        this.productivity = productivity;
        this.popularity = popularity;
        this.step = step;
    }

    // Getters and Setters
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

    public static CycleDto fromEntity(Cycle cycle){
        return new CycleDto(cycle.getId(), cycle.getCost(), cycle.getEmployees(), cycle.getProductivity(), cycle.getPopularity(), cycle.getStep());
    }
}
