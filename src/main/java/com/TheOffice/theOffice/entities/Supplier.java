package com.TheOffice.theOffice.entities;

import java.math.BigDecimal;

public class Supplier {
    private Long id;
    private String name;
    private BigDecimal price;
    private String quality;
    private Long id_company;

    //Constructor
    public Supplier(Long id, String name, BigDecimal price, String quality, Long id_company) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quality = quality;
        this.id_company = id_company;
    }

    public Supplier() {
    }

    //Getters and Setters
    public long getId() {
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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    public Long getId_company() {
        return id_company;
    }

    public void setId_company(Long id_company) {
        this.id_company = id_company;
    }
}
