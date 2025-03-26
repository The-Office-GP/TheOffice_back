package com.TheOffice.theOffice.entities;

public class StockFinalMaterial {
    private Long id;
    private String name;
    private Integer quantityLow;
    private Integer quantityMid;
    private Integer quantityHigh;
    private Integer proportionProduct;
    private Integer quantityToProduct;
    private Integer monthProduction;
    private Integer sell;
    private Integer price;
    private Long companyId;

    public StockFinalMaterial() {
    }

    public StockFinalMaterial(Long id, String name, Integer quantityLow, Integer quantityMid, Integer quantityHigh, Integer proportionProduct, Integer quantityToProduct, Integer monthProduction, Integer sell, Integer price, Long companyId) {
        this.id = id;
        this.name = name;
        this.quantityLow = quantityLow;
        this.quantityMid = quantityMid;
        this.quantityHigh = quantityHigh;
        this.proportionProduct = proportionProduct;
        this.quantityToProduct = quantityToProduct;
        this.monthProduction = monthProduction;
        this.sell = sell;
        this.price = price;
        this.companyId = companyId;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getSell() {
        return sell;
    }

    public void setSell(Integer sell) {
        this.sell = sell;
    }

    public Integer getMonthProduction() {
        return monthProduction;
    }

    public void setMonthProduction(Integer monthProduction) {
        this.monthProduction = monthProduction;
    }

    public Integer getQuantityToProduct() {
        return quantityToProduct;
    }

    public void setQuantityToProduct(Integer quantityToProduct) {
        this.quantityToProduct = quantityToProduct;
    }

    public Integer getProportionProduct() {
        return proportionProduct;
    }

    public void setProportionProduct(Integer proportionProduct) {
        this.proportionProduct = proportionProduct;
    }

    public Integer getQuantityHigh() {
        return quantityHigh;
    }

    public void setQuantityHigh(Integer quantityHigh) {
        this.quantityHigh = quantityHigh;
    }

    public Integer getQuantityMid() {
        return quantityMid;
    }

    public void setQuantityMid(Integer quantityMid) {
        this.quantityMid = quantityMid;
    }

    public Integer getQuantityLow() {
        return quantityLow;
    }

    public void setQuantityLow(Integer quantityLow) {
        this.quantityLow = quantityLow;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
