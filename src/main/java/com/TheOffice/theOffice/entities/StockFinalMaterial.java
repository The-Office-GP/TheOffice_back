package com.TheOffice.theOffice.entities;

public class StockFinalMaterial {
    private Long id;
    private String name;
    private Integer quality;
    private Integer quantity;
    private Integer proportionProduct;
    private Integer quantityToProduct;
    private Integer monthProduction;
    private Integer sell;
    private Integer monthSell;
    private Long companyId;

    public StockFinalMaterial() {
    }

    public StockFinalMaterial(Long id, String name, Integer quality, Integer quantity, Integer proportionProduct, Integer quantityToProduct, Integer monthProduction, Integer sell, Integer monthSell, Long companyId) {
        this.id = id;
        this.name = name;
        this.quality = quality;
        this.quantity = quantity;
        this.proportionProduct = proportionProduct;
        this.quantityToProduct = quantityToProduct;
        this.monthProduction = monthProduction;
        this.sell = sell;
        this.monthSell = monthSell;
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

    public Integer getQuality() {
        return quality;
    }

    public void setQuality(Integer quality) {
        this.quality = quality;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getProportionProduct() {
        return proportionProduct;
    }

    public void setProportionProduct(Integer proportionProduct) {
        this.proportionProduct = proportionProduct;
    }

    public Integer getQuantityToProduct() {
        return quantityToProduct;
    }

    public void setQuantityToProduct(Integer quantityToProduct) {
        this.quantityToProduct = quantityToProduct;
    }

    public Integer getmonthProduction() {
        return monthProduction;
    }

    public void setmonthProduction(Integer monthProduction) {
        this.monthProduction = monthProduction;
    }

    public Integer getSell() {
        return sell;
    }

    public void setSell(Integer sell) {
        this.sell = sell;
    }

    public Integer getmonthSell() {
        return monthSell;
    }

    public void setmonthSell(Integer monthSell) {
        this.monthSell = monthSell;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }
}
