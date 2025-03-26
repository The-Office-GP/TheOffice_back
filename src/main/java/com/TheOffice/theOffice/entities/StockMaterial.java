package com.TheOffice.theOffice.entities;

public class StockMaterial {
    private Long id;
    private String name;
    private Integer quantityLow;
    private Integer quantityMid;
    private Integer quantityHigh;
    private Long companyId;

    public StockMaterial() {
    }

    // Constructor

    public StockMaterial(Long id, String name, Integer quantityLow, Integer quantityMid, Integer quantityHigh, Long companyId) {
        this.id = id;
        this.name = name;
        this.quantityLow = quantityLow;
        this.quantityMid = quantityMid;
        this.quantityHigh = quantityHigh;
        this.companyId = companyId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getQuantityLow() {
        return quantityLow;
    }

    public void setQuantityLow(Integer quantityLow) {
        this.quantityLow = quantityLow;
    }

    public Integer getQuantityMid() {
        return quantityMid;
    }

    public void setQuantityMid(Integer quantityMid) {
        this.quantityMid = quantityMid;
    }

    public Integer getQuantityHigh() {
        return quantityHigh;
    }

    public void setQuantityHigh(Integer quantityHigh) {
        this.quantityHigh = quantityHigh;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }
}