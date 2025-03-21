package com.TheOffice.theOffice.entities;

import java.util.Optional;

public class Cycle {
    private Long id;
    private Integer step;
    private Integer productionSpeed;
    private Integer priorityProduction;
    private Integer priorityMarketing;
    private Integer countGoodSell;
    private Integer countBadSell;
    private Long companyId;

    public Cycle(){
    }

    public Cycle(Long id, Integer step, Integer productionSpeed, Integer priorityProduction, Integer priorityMarketing, Integer countGoodSell, Integer countBadSell, Long companyId) {
        this.id = id;
        this.step = step;
        this.productionSpeed = productionSpeed;
        this.priorityProduction = priorityProduction;
        this.priorityMarketing = priorityMarketing;
        this.countGoodSell = countGoodSell;
        this.countBadSell = countBadSell;
        this.companyId = companyId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getStep() {
        return step;
    }

    public void setStep(Integer cycle) {
        this.step = cycle;
    }

    public Integer getProductionSpeed() {
        return productionSpeed;
    }

    public void setProductionSpeed(Integer productionSpeed) {
        this.productionSpeed = productionSpeed;
    }

    public Integer getPriorityProduction() {
        return priorityProduction;
    }

    public void setPriorityProduction(Integer priorityProduction) {
        this.priorityProduction = priorityProduction;
    }

    public Integer getPriorityMarketing() {
        return priorityMarketing;
    }

    public void setPriorityMarketing(Integer priorityMarketing) {
        this.priorityMarketing = priorityMarketing;
    }

    public Integer getCountGoodSell() {
        return countGoodSell;
    }

    public void setCountGoodSell(Integer countGoodSell) {
        this.countGoodSell = countGoodSell;
    }

    public Integer getCountBadSell() {
        return countBadSell;
    }

    public void setCountBadSell(Integer countBadSell) {
        this.countBadSell = countBadSell;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }
}
