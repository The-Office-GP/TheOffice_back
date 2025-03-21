package com.TheOffice.theOffice.dtos;

import com.TheOffice.theOffice.entities.Cycle;

public class CycleDto {
    private Long id;
    private Integer productionSpeed;
    private Integer priorityProduction;
    private Integer countGoodSell;
    private Integer countBadSell;
    private Long companyId;

    public CycleDto() {}

    public CycleDto(Long id, Integer productionSpeed, Integer priorityProduction, Integer countGoodSell, Integer countBadSell, Long companyId) {
        this.id = id;
        this.productionSpeed = productionSpeed;
        this.priorityProduction = priorityProduction;
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

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
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

    public static CycleDto fromEntity(Cycle cycle){
        return new CycleDto(cycle.getId(), cycle.getProductionSpeed(), cycle.getPriorityProduction(), cycle.getCountGoodSell() , cycle.getCountBadSell(), cycle.getCompanyId());
    }
}
