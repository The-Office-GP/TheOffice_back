package com.TheOffice.theOffice.staticModels.Suppliers;

public class Supplier {
    private Long id;
    private String name;
    private Integer levelQuality;
    private Integer price;

    public Supplier(){}

    public Supplier(Long id, String name, Integer levelQuality, Integer price){
        this.id = id;
        this.name = name;
        this.levelQuality = levelQuality;
        this.price = price;
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
