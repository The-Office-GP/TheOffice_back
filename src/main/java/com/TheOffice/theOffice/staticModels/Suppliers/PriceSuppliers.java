package com.TheOffice.theOffice.staticModels.Suppliers;

public class PriceSuppliers {
    private Integer levelQuality;
    private Integer price;

    public PriceSuppliers() {
    }

    public PriceSuppliers(Integer levelQuality, Integer price) {
        this.levelQuality = levelQuality;
        this.price = price;
    }

    public Integer getLevelQuality() {
        return levelQuality;
    }

    public void setLevelQuality(Integer levelQuality) {
        this.levelQuality = levelQuality;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }
}
